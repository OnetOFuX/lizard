package com.lizardstore.ordenms.servicio;

import com.lizardstore.ordenms.entidad.Orden;
import com.lizardstore.ordenms.entidad.OrdenItem;
import com.lizardstore.ordenms.evento.EventoCarrito;
import com.lizardstore.ordenms.evento.EventoOrden;
import com.lizardstore.ordenms.evento.EventoPago;
import com.lizardstore.ordenms.repositorio.OrdenRepositorio;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdenServicio {

    public static final String ESTADO_PENDIENTE_PAGO = "PENDIENTE_PAGO";
    public static final String ESTADO_CONFIRMADA = "CONFIRMADA";
    public static final String ESTADO_CANCELADA = "CANCELADA";

    private static final String TIPO_ORDEN_CREADA = "orden.creada";
    private static final String TIPO_ORDEN_CONFIRMADA = "orden.confirmada";
    private static final String TIPO_ORDEN_CANCELADA = "orden.cancelada";

    private final OrdenRepositorio ordenRepositorio;
    private final ProductorOrden productorOrden;

    @Value("${spring.application.name}")
    private String applicationName;

    @Transactional(readOnly = true)
    public List<Orden> listarOrdenes() {
        return ordenRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public List<Orden> listarOrdenesPorUsuario(Long usuarioId) {
        return ordenRepositorio.findByUsuarioIdOrderByIdDesc(usuarioId);
    }

    @Transactional
    public Orden crearOrden(Orden orden) {
        orden.setId(null);
        orden.setEstado(ESTADO_PENDIENTE_PAGO);
        Orden guardada = ordenRepositorio.save(orden);
        publicarEvento(guardada, TIPO_ORDEN_CREADA);
        return guardada;
    }

    @Transactional
    public void crearOrdenDesdeCarrito(EventoCarrito evento) {
        if (evento.getCarritoId() != null) {
            Optional<Orden> existente = ordenRepositorio.findByCarritoId(evento.getCarritoId());
            if (existente.isPresent()) {
                log.warn("service=orden-ms carritoId={} status=already-processed", evento.getCarritoId());
                return;
            }
        }

        Orden orden = Orden.builder()
                .usuarioId(evento.getUsuarioId())
                .carritoId(evento.getCarritoId())
                .total(evento.getSubtotal())
                .estado(ESTADO_PENDIENTE_PAGO)
                .build();

        if (evento.getItems() != null) {
            evento.getItems().forEach(item -> orden.getItems().add(OrdenItem.builder()
                    .orden(orden)
                    .productoId(item.getProductoId())
                    .nombreProducto(item.getNombreProducto())
                    .precioUnitario(item.getPrecioUnitario())
                    .cantidad(item.getCantidad())
                    .subtotalLinea(item.getSubtotalLinea())
                    .build()));
        }

        Orden guardada = ordenRepositorio.save(orden);
        publicarEvento(guardada, TIPO_ORDEN_CREADA);
        log.info("service=orden-ms ordenId={} carritoId={} status=created-from-checkout",
                guardada.getId(), guardada.getCarritoId());
    }

    @Transactional
    public void procesarEventoPago(EventoPago evento) {
        if (evento.getOrdenId() == null) {
            return;
        }

        Orden orden = ordenRepositorio.findById(evento.getOrdenId())
                .orElse(null);
        if (orden == null) {
            log.warn("service=orden-ms ordenId={} status=not-found", evento.getOrdenId());
            return;
        }

        if ("pago.realizado".equals(evento.getTipoEvento())) {
            orden.setEstado(ESTADO_CONFIRMADA);
            ordenRepositorio.save(orden);
            publicarEvento(orden, TIPO_ORDEN_CONFIRMADA);
        } else if ("pago.fallido".equals(evento.getTipoEvento())) {
            orden.setEstado(ESTADO_CANCELADA);
            ordenRepositorio.save(orden);
            publicarEvento(orden, TIPO_ORDEN_CANCELADA);
        }
    }

    private void publicarEvento(Orden orden, String tipoEvento) {
        BigDecimal total = orden.getTotal() != null ? orden.getTotal() : BigDecimal.ZERO;

        List<EventoOrden.ItemEvento> itemEventos = null;
        if (orden.getItems() != null && !orden.getItems().isEmpty()) {
            itemEventos = orden.getItems().stream()
                    .map(item -> EventoOrden.ItemEvento.builder()
                            .productoId(item.getProductoId())
                            .cantidad(item.getCantidad())
                            .build())
                    .toList();
        }

        EventoOrden evento = EventoOrden.builder()
                .tipoEvento(tipoEvento)
                .ordenId(orden.getId())
                .usuarioId(orden.getUsuarioId())
                .carritoId(orden.getCarritoId())
                .total(total.doubleValue())
                .estado(orden.getEstado())
                .items(itemEventos)
                .origen(applicationName)
                .timestamp(Instant.now().toEpochMilli())
                .build();
        productorOrden.publicarOrdenCreada(evento);
    }
}
