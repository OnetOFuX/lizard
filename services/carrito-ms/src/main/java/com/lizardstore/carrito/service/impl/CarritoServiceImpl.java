package com.lizardstore.carrito.service.impl;

import com.lizardstore.carrito.client.ProductoClient;
import com.lizardstore.carrito.dto.ActualizarItemRequest;
import com.lizardstore.carrito.dto.AgregarItemRequest;
import com.lizardstore.carrito.dto.CarritoResponse;
import com.lizardstore.carrito.dto.ProductoSnapshotDto;
import com.lizardstore.carrito.entity.Carrito;
import com.lizardstore.carrito.entity.CarritoItem;
import com.lizardstore.carrito.event.EventoCarrito;
import com.lizardstore.carrito.exception.BusinessException;
import com.lizardstore.carrito.exception.ResourceNotFoundException;
import com.lizardstore.carrito.mapper.CarritoMapper;
import com.lizardstore.carrito.messaging.CarritoEventProducer;
import com.lizardstore.carrito.repository.CarritoRepository;
import com.lizardstore.carrito.service.CarritoService;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

    private static final String ESTADO_ACTIVO = "ACTIVO";
    private static final String ESTADO_CONFIRMADO = "CONFIRMADO";
    private static final String EVENTO_ACTUALIZADO = "carrito.actualizado";
    private static final String EVENTO_CONFIRMADO = "carrito.confirmado";

    private final CarritoRepository carritoRepository;
    private final ProductoClient productoClient;
    private final CarritoMapper carritoMapper;
    private final CarritoEventProducer eventProducer;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    @Transactional
    public CarritoResponse obtenerCarrito(Long usuarioId) {
        Carrito carrito = obtenerOCrearCarritoActivo(usuarioId);
        return carritoMapper.toResponse(carrito);
    }

    @Override
    @Transactional
    public CarritoResponse agregarItem(Long usuarioId, AgregarItemRequest request) {
        Carrito carrito = obtenerOCrearCarritoActivo(usuarioId);
        ProductoSnapshotDto producto = validarProducto(request.getProductoId(), request.getCantidad());

        Optional<CarritoItem> existente = carrito.getItems().stream()
                .filter(i -> i.getProductoId().equals(request.getProductoId()))
                .findFirst();

        if (existente.isPresent()) {
            CarritoItem item = existente.get();
            int nuevaCantidad = item.getCantidad() + request.getCantidad();
            validarStock(producto, nuevaCantidad);
            item.setCantidad(nuevaCantidad);
            item.setPrecioUnitario(producto.getPrecio());
            item.setSubtotalLinea(calcularLinea(producto.getPrecio(), nuevaCantidad));
        } else {
            carrito.getItems().add(CarritoItem.builder()
                    .carrito(carrito)
                    .productoId(request.getProductoId())
                    .nombreProducto(producto.getNombre())
                    .precioUnitario(producto.getPrecio())
                    .cantidad(request.getCantidad())
                    .subtotalLinea(calcularLinea(producto.getPrecio(), request.getCantidad()))
                    .build());
        }

        recalcularSubtotal(carrito);
        Carrito guardado = carritoRepository.save(carrito);
        publicarEvento(guardado, EVENTO_ACTUALIZADO, true);
        return carritoMapper.toResponse(guardado);
    }

    @Override
    @Transactional
    public CarritoResponse actualizarItem(Long usuarioId, Long productoId, ActualizarItemRequest request) {
        Carrito carrito = obtenerCarritoActivo(usuarioId);
        CarritoItem item = buscarItem(carrito, productoId);
        ProductoSnapshotDto producto = validarProducto(productoId, request.getCantidad());

        item.setCantidad(request.getCantidad());
        item.setPrecioUnitario(producto.getPrecio());
        item.setSubtotalLinea(calcularLinea(producto.getPrecio(), request.getCantidad()));

        recalcularSubtotal(carrito);
        carrito.setUpdatedAt(LocalDateTime.now());
        Carrito guardado = carritoRepository.save(carrito);
        publicarEvento(guardado, EVENTO_ACTUALIZADO, true);
        return carritoMapper.toResponse(guardado);
    }

    @Override
    @Transactional
    public CarritoResponse eliminarItem(Long usuarioId, Long productoId) {
        Carrito carrito = obtenerCarritoActivo(usuarioId);
        CarritoItem item = buscarItem(carrito, productoId);
        carrito.getItems().remove(item);
        recalcularSubtotal(carrito);
        carrito.setUpdatedAt(LocalDateTime.now());
        Carrito guardado = carritoRepository.save(carrito);
        publicarEvento(guardado, EVENTO_ACTUALIZADO, true);
        return carritoMapper.toResponse(guardado);
    }

    @Override
    @Transactional
    public void vaciarCarrito(Long usuarioId) {
        Carrito carrito = obtenerCarritoActivo(usuarioId);
        carrito.getItems().clear();
        carrito.setSubtotal(BigDecimal.ZERO);
        carrito.setUpdatedAt(LocalDateTime.now());
        carritoRepository.save(carrito);
        publicarEvento(carrito, EVENTO_ACTUALIZADO, false);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularSubtotal(Long usuarioId) {
        return obtenerCarritoActivo(usuarioId).getSubtotal();
    }

    @Override
    @Transactional
    public CarritoResponse checkout(Long usuarioId) {
        Carrito carrito = obtenerCarritoActivo(usuarioId);
        if (carrito.getItems().isEmpty()) {
            throw new BusinessException("No se puede confirmar un carrito vacio");
        }

        carrito.setEstado(ESTADO_CONFIRMADO);
        carrito.setUpdatedAt(LocalDateTime.now());
        Carrito guardado = carritoRepository.save(carrito);
        publicarEvento(guardado, EVENTO_CONFIRMADO, false);

        log.info("service=carrito-ms checkout usuarioId={} carritoId={} subtotal={}",
                usuarioId, guardado.getId(), guardado.getSubtotal());

        return carritoMapper.toResponse(guardado);
    }

    private Carrito obtenerCarritoActivo(Long usuarioId) {
        return carritoRepository.findByUsuarioIdAndEstadoWithItems(usuarioId, ESTADO_ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe carrito activo para el usuario " + usuarioId));
    }

    private Carrito obtenerOCrearCarritoActivo(Long usuarioId) {
        return carritoRepository.findByUsuarioIdAndEstadoWithItems(usuarioId, ESTADO_ACTIVO)
                .orElseGet(() -> {
                    LocalDateTime now = LocalDateTime.now();
                    Carrito nuevo = Carrito.builder()
                            .usuarioId(usuarioId)
                            .estado(ESTADO_ACTIVO)
                            .subtotal(BigDecimal.ZERO)
                            .createdAt(now)
                            .updatedAt(now)
                            .build();
                    return carritoRepository.save(nuevo);
                });
    }

    private CarritoItem buscarItem(Carrito carrito, Long productoId) {
        return carrito.getItems().stream()
                .filter(i -> i.getProductoId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto " + productoId + " no encontrado en el carrito"));
    }

    private ProductoSnapshotDto validarProducto(Long productoId, int cantidad) {
        ProductoSnapshotDto producto;
        try {
            producto = productoClient.obtenerProducto(productoId.intValue());
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Producto no encontrado: " + productoId);
        }
        if (producto == null || Boolean.FALSE.equals(producto.getActivo())) {
            throw new BusinessException("El producto no esta disponible");
        }
        if (producto.getPrecio() == null || producto.getPrecio().signum() <= 0) {
            throw new BusinessException("El producto no tiene precio valido");
        }
        validarStock(producto, cantidad);
        return producto;
    }

    private void validarStock(ProductoSnapshotDto producto, int cantidad) {
        if (producto.getStock() != null && cantidad > producto.getStock()) {
            throw new BusinessException("Stock insuficiente para el producto " + producto.getNombre());
        }
    }

    private BigDecimal calcularLinea(BigDecimal precio, int cantidad) {
        return precio.multiply(BigDecimal.valueOf(cantidad));
    }

    private void recalcularSubtotal(Carrito carrito) {
        BigDecimal subtotal = carrito.getItems().stream()
                .map(CarritoItem::getSubtotalLinea)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        carrito.setSubtotal(subtotal);
        carrito.setUpdatedAt(LocalDateTime.now());
    }

    private void publicarEvento(Carrito carrito, String tipoEvento, boolean topicCarrito) {
        EventoCarrito evento = EventoCarrito.builder()
                .tipoEvento(tipoEvento)
                .carritoId(carrito.getId())
                .usuarioId(carrito.getUsuarioId())
                .subtotal(carrito.getSubtotal())
                .items(carritoMapper.toItemEventos(carrito))
                .origen(applicationName)
                .timestamp(Instant.now().toEpochMilli())
                .build();

        if (EVENTO_CONFIRMADO.equals(tipoEvento)) {
            eventProducer.publicarCarritoConfirmado(evento);
        } else if (topicCarrito) {
            eventProducer.publicarCarritoActualizado(evento);
        }
    }
}
