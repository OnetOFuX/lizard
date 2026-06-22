package com.lizardstore.producto.service.impl;

import com.lizardstore.producto.client.CatalogoClient;
import com.lizardstore.producto.dto.CategoriaDto;
import com.lizardstore.producto.dto.ProductoRequest;
import com.lizardstore.producto.dto.ProductoResponse;
import com.lizardstore.producto.entity.Producto;
import com.lizardstore.producto.event.EventoProducto;
import com.lizardstore.producto.exception.ResourceNotFoundException;
import com.lizardstore.producto.mapper.ProductoMapper;
import com.lizardstore.producto.messaging.ProductoEventProducer;
import com.lizardstore.producto.repository.ProductoRepository;
import com.lizardstore.producto.service.ProductoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoServiceImpl implements ProductoService {

    private static final String EVENTO_CREADO = "producto.creado";
    private static final String EVENTO_ACTUALIZADO = "producto.actualizado";

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final CatalogoClient catalogoClient;
    private final ProductoEventProducer eventProducer;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    @Transactional
    public ProductoResponse create(ProductoRequest request) {
        log.info("Iniciando creacion de producto con nombre: {} y idCategoria: {}", request.getNombre(),
                request.getIdCategoria());
        Producto producto = productoMapper.toEntity(request);
        Producto savedProducto = productoRepository.save(producto);
        log.info("Producto creado exitosamente con ID: {}", savedProducto.getId());
        publicarEvento(savedProducto, EVENTO_CREADO);
        return productoMapper.toResponse(savedProducto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> findAll() {
        log.info("Recuperando lista de productos");
        List<ProductoResponse> productos = productoRepository.findAll()
                .stream()
                .map(productoMapper::toResponse)
                .toList();
        log.info("Se encontraron {} productos", productos.size());
        return productos;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse findById(Integer id) {
        log.info("Buscando producto con ID: {}", id);
        Producto producto = getProductoById(id);
        log.info("Producto encontrado: {} (ID: {})", producto.getNombre(), id);
        return productoMapper.toResponse(producto);
    }

    @Override
    @Transactional
    public ProductoResponse update(Integer id, ProductoRequest request) {
        log.info("Iniciando actualizacion de producto ID: {}", id);
        Producto producto = getProductoById(id);
        productoMapper.updateEntityFromRequest(producto, request);
        Producto updatedProducto = productoRepository.save(producto);
        log.info("Producto ID: {} actualizado exitosamente", id);
        publicarEvento(updatedProducto, EVENTO_ACTUALIZADO);
        return productoMapper.toResponse(updatedProducto);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Iniciando eliminacion de producto ID: {}", id);
        getProductoById(id);
        productoRepository.deleteById(id);
        log.info("Producto ID: {} eliminado exitosamente", id);
    }

    private Producto getProductoById(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado: ID {}", id);
                    return new ResourceNotFoundException("Producto con id " + id + " no encontrado");
                });
    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "catalogo", fallbackMethod = "fallbackCategoria")
    public ProductoResponse findDetalleById(Integer id) {
        log.info("[PRODUCTO] Buscando detalle de producto con ID: {}", id);

        Producto producto = getProductoById(id);
        log.info("[PRODUCTO] Consultando categoriaId={} en catalogo", producto.getIdCategoria());

        CategoriaDto categoria = catalogoClient.findCategoriaById(
                producto.getIdCategoria().longValue());

        ProductoResponse response = productoMapper.toResponse(producto);
        response.setCategoria(categoria);
        return response;
    }

    public ProductoResponse fallbackCategoria(Integer id, Throwable ex) {
        log.warn("[PRODUCTO] Fallback activado para producto ID {}. Motivo: {}", id, ex.getMessage());
        return productoMapper.toResponse(getProductoById(id));
    }

    private void publicarEvento(Producto producto, String tipoEvento) {
        EventoProducto evento = EventoProducto.builder()
                .tipoEvento(tipoEvento)
                .productoId(producto.getId())
                .sku(producto.getSku())
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .categoriaId(producto.getIdCategoria())
                .activo(producto.getActivo())
                .origen(applicationName)
                .timestamp(Instant.now().toEpochMilli())
                .build();
        eventProducer.publicar(evento);
    }

    @Override
    @Transactional
    public void uploadImage(Integer id, org.springframework.web.multipart.MultipartFile file) {
        log.info("Iniciando subida de imagen para producto ID: {}", id);
        Producto producto = getProductoById(id);
        try {
            producto.setImagen(file.getBytes());
            producto.setImagenMimeType(file.getContentType());
            productoRepository.save(producto);
            log.info("Imagen subida exitosamente para producto ID: {}", id);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Error al leer el archivo de imagen", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findByIdEntity(Integer id) {
        return getProductoById(id);
    }

    @Override
    @Transactional
    public void descontarStock(Integer productoId, int cantidad) {
        Producto producto = getProductoById(productoId);
        int stockActual = producto.getStock() != null ? producto.getStock() : 0;
        int nuevoStock = Math.max(0, stockActual - cantidad);
        producto.setStock(nuevoStock);
        Producto guardado = productoRepository.save(producto);
        log.info("Stock descontado: productoId={} stockAnterior={} cantidad={} nuevoStock={}",
                productoId, stockActual, cantidad, nuevoStock);
        publicarEvento(guardado, EVENTO_ACTUALIZADO);
    }
}
