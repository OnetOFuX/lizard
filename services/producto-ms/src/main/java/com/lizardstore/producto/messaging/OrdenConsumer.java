package com.lizardstore.producto.messaging;

import com.lizardstore.producto.event.EventoOrden;
import com.lizardstore.producto.service.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrdenConsumer {

    private static final String TIPO_ORDEN_CONFIRMADA = "orden.confirmada";

    private final ProductoService productoService;

    @KafkaListener(
            topics = "${app.kafka.topic.ordenes}",
            groupId = "${app.kafka.group-id.ordenes-stock}",
            containerFactory = "ordenListenerContainerFactory"
    )
    public void consumirEventoOrden(EventoOrden evento) {
        if (evento == null || !TIPO_ORDEN_CONFIRMADA.equals(evento.getTipoEvento())) {
            log.debug("service=producto-ms eventType={} status=ignored",
                    evento != null ? evento.getTipoEvento() : null);
            return;
        }

        log.info("service=producto-ms ordenId={} status=order-confirmed-stock-update",
                evento.getOrdenId());

        if (evento.getItems() == null || evento.getItems().isEmpty()) {
            log.warn("service=producto-ms ordenId={} status=no-items-to-decrement", evento.getOrdenId());
            return;
        }

        for (EventoOrden.ItemEvento item : evento.getItems()) {
            try {
                productoService.descontarStock(item.getProductoId().intValue(), item.getCantidad());
                log.info("service=producto-ms ordenId={} productoId={} cantidad={} status=stock-decremented",
                        evento.getOrdenId(), item.getProductoId(), item.getCantidad());
            } catch (Exception e) {
                log.error("service=producto-ms ordenId={} productoId={} status=stock-decrement-failed error=\"{}\"",
                        evento.getOrdenId(), item.getProductoId(), e.getMessage());
            }
        }
    }
}
