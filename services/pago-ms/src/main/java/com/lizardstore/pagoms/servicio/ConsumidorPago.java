package com.lizardstore.pagoms.servicio;

import com.lizardstore.pagoms.evento.EventoOrden;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumidorPago {

    private static final String TIPO_EVENTO_ORDEN_CREADA = "orden.creada";

    private final PagoServicio pagoServicio;
    @Value("${app.kafka.topic.ordenes}")
    private String topicOrdenes;
    @Value("${app.kafka.group-id.pagos}")
    private String groupIdPagos;

    @KafkaListener(topics = "${app.kafka.topic.ordenes}", groupId = "${app.kafka.group-id.pagos}", containerFactory = "kafkaListenerContainerFactory")
    public void consumirEventoOrden(EventoOrden eventoOrden) {
        if (eventoOrden == null || !TIPO_EVENTO_ORDEN_CREADA.equals(eventoOrden.getTipoEvento())) {
            log.warn("service=pago-ms component=consumer eventType={} status=ignored",
                    eventoOrden != null ? eventoOrden.getTipoEvento() : null);
            return;
        }

        long processedAt = Instant.now().toEpochMilli();
        Long latencyMs = eventoOrden.getTimestamp() != null ? processedAt - eventoOrden.getTimestamp() : null;

        log.info(
                "service=pago-ms component=consumer topic={} groupId={} eventType={} ordenId={} timestamp={} processedAt={} latencyMs={} status=consumed",
                topicOrdenes,
                groupIdPagos,
                eventoOrden.getTipoEvento(),
                eventoOrden.getOrdenId(),
                eventoOrden.getTimestamp(),
                processedAt,
                latencyMs);

        pagoServicio.iniciarPagoParaOrden(eventoOrden);
    }
}
