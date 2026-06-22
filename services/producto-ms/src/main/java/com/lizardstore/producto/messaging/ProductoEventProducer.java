package com.lizardstore.producto.messaging;

import com.lizardstore.producto.event.EventoProducto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductoEventProducer {

    private final KafkaTemplate<String, EventoProducto> kafkaTemplate;

    @Value("${app.kafka.topic.productos}")
    private String topicProductos;

    public void publicar(EventoProducto evento) {
        kafkaTemplate.send(topicProductos, String.valueOf(evento.getProductoId()), evento)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("service=producto-ms topic={} eventType={} productoId={} status=error error=\"{}\"",
                                topicProductos, evento.getTipoEvento(), evento.getProductoId(), ex.getMessage());
                        return;
                    }
                    log.info("service=producto-ms topic={} eventType={} productoId={} status=published",
                            topicProductos, evento.getTipoEvento(), evento.getProductoId());
                });
    }
}
