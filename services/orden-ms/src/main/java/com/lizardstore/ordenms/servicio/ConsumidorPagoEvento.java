package com.lizardstore.ordenms.servicio;

import com.lizardstore.ordenms.evento.EventoPago;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumidorPagoEvento {

    private final OrdenServicio ordenServicio;

    @KafkaListener(
            topics = "${app.kafka.topic.pagos}",
            groupId = "${app.kafka.group-id.ordenes-pagos}",
            containerFactory = "pagoListenerContainerFactory"
    )
    public void consumirPago(EventoPago evento) {
        if (evento == null) {
            return;
        }
        log.info("service=orden-ms eventType={} ordenId={} status=payment-event-received",
                evento.getTipoEvento(), evento.getOrdenId());
        ordenServicio.procesarEventoPago(evento);
    }
}
