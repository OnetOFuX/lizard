package com.lizardstore.ordenms.servicio;

import com.lizardstore.ordenms.evento.EventoCarrito;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumidorCarrito {

    private static final String TIPO_CARRITO_CONFIRMADO = "carrito.confirmado";

    private final OrdenServicio ordenServicio;

    @KafkaListener(
            topics = "${app.kafka.topic.checkout}",
            groupId = "${app.kafka.group-id.ordenes}",
            containerFactory = "carritoListenerContainerFactory"
    )
    public void consumirCheckout(EventoCarrito evento) {
        if (evento == null || !TIPO_CARRITO_CONFIRMADO.equals(evento.getTipoEvento())) {
            log.warn("service=orden-ms eventType={} status=ignored",
                    evento != null ? evento.getTipoEvento() : null);
            return;
        }
        log.info("service=orden-ms carritoId={} usuarioId={} status=checkout-received",
                evento.getCarritoId(), evento.getUsuarioId());
        ordenServicio.crearOrdenDesdeCarrito(evento);
    }
}
