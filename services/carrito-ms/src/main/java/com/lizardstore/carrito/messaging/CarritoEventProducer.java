package com.lizardstore.carrito.messaging;

import com.lizardstore.carrito.event.EventoCarrito;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CarritoEventProducer {

    private final KafkaTemplate<String, EventoCarrito> kafkaTemplate;

    @Value("${app.kafka.topic.carrito}")
    private String topicCarrito;

    @Value("${app.kafka.topic.checkout}")
    private String topicCheckout;

    public void publicarCarritoActualizado(EventoCarrito evento) {
        enviar(topicCarrito, evento);
    }

    public void publicarCarritoConfirmado(EventoCarrito evento) {
        enviar(topicCheckout, evento);
    }

    private void enviar(String topic, EventoCarrito evento) {
        kafkaTemplate.send(topic, String.valueOf(evento.getCarritoId()), evento)
                .whenComplete((resultado, ex) -> {
                    if (ex != null) {
                        log.error("service=carrito-ms topic={} eventType={} status=error error=\"{}\"",
                                topic, evento.getTipoEvento(), ex.getMessage());
                        return;
                    }
                    log.info("service=carrito-ms topic={} eventType={} carritoId={} status=published",
                            topic, evento.getTipoEvento(), evento.getCarritoId());
                });
    }
}
