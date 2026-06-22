package com.lizardstore.pagoms.servicio;

import com.lizardstore.pagoms.dto.PreferenciaResponse;
import com.lizardstore.pagoms.entidad.Pago;
import com.lizardstore.pagoms.evento.EventoOrden;
import com.lizardstore.pagoms.evento.EventoPago;
import com.lizardstore.pagoms.repositorio.PagoRepositorio;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagoServicio {

    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_APROBADO = "APROBADO";
    public static final String ESTADO_RECHAZADO = "RECHAZADO";

    private static final String TIPO_EVENTO_PAGO_REALIZADO = "pago.realizado";
    private static final String TIPO_EVENTO_PAGO_FALLIDO = "pago.fallido";
    private static final String MP_STATUS_APPROVED = "approved";

    private final PagoRepositorio pagoRepositorio;
    private final MercadoPagoServicio mercadoPagoServicio;
    private final ProductorPago productorPago;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${mercadopago.public-key:}")
    private String publicKey;

    public List<Pago> listarPagos() {
        return pagoRepositorio.findAll();
    }

    public Optional<Pago> buscarPagoPorId(Long id) {
        return pagoRepositorio.findById(id);
    }

    public Optional<Pago> buscarPagoPorOrdenId(Long ordenId) {
        return pagoRepositorio.findTopByOrdenIdOrderByIdDesc(ordenId);
    }

    @Transactional
    public void iniciarPagoParaOrden(EventoOrden eventoOrden) {
        if (eventoOrden.getOrdenId() == null || eventoOrden.getTotal() == null) {
            return;
        }

        Optional<Pago> existente = pagoRepositorio.findTopByOrdenIdOrderByIdDesc(eventoOrden.getOrdenId());
        if (existente.isPresent()) {
            log.info("service=pago-ms ordenId={} status=payment-already-initiated", eventoOrden.getOrdenId());
            return;
        }

        Map<String, String> preferencia = mercadoPagoServicio.crearPreferencia(
                eventoOrden.getOrdenId(),
                eventoOrden.getTotal());

        Pago pago = Pago.builder()
                .ordenId(eventoOrden.getOrdenId())
                .monto(eventoOrden.getTotal())
                .estado(ESTADO_PENDIENTE)
                .preferenciaId(preferencia.get("preferenciaId"))
                .initPoint(preferencia.get("initPoint"))
                .build();

        pagoRepositorio.save(pago);
        log.info("service=pago-ms ordenId={} preferenciaId={} status=payment-initiated",
                eventoOrden.getOrdenId(), pago.getPreferenciaId());
    }

    @Transactional
    public PreferenciaResponse obtenerPreferencia(Long ordenId, Double monto) {
        Pago pago = pagoRepositorio.findTopByOrdenIdOrderByIdDesc(ordenId)
                .orElse(null);

        if (pago == null) {
            if (monto == null || monto <= 0) {
                throw new IllegalArgumentException("No existe pago para la orden " + ordenId + " y no se especificó un monto válido");
            }
            log.info("service=pago-ms ordenId={} status=lazy-creating-payment-record monto={}", ordenId, monto);
            pago = Pago.builder()
                    .ordenId(ordenId)
                    .monto(monto)
                    .estado(ESTADO_PENDIENTE)
                    .build();
            pago = pagoRepositorio.save(pago);
        }

        // Si el pago local está PENDIENTE, verificamos si ya se registró la aprobación en Mercado Pago
        if (ESTADO_PENDIENTE.equals(pago.getEstado())) {
            Map<String, Object> mpPago = mercadoPagoServicio.buscarPagoPorOrdenId(ordenId);
            if (mpPago != null) {
                String mpStatus = (String) mpPago.get("status");
                String paymentId = (String) mpPago.get("id");
                if (MP_STATUS_APPROVED.equals(mpStatus)) {
                    pago.setMercadopagoPaymentId(paymentId);
                    finalizarPago(pago, ESTADO_APROBADO, TIPO_EVENTO_PAGO_REALIZADO);
                } else if ("rejected".equals(mpStatus) || "cancelled".equals(mpStatus)) {
                    pago.setMercadopagoPaymentId(paymentId);
                    finalizarPago(pago, ESTADO_RECHAZADO, TIPO_EVENTO_PAGO_FALLIDO);
                }
            }
        }

        boolean esSimulado = pago.getPreferenciaId() == null || pago.getPreferenciaId().startsWith("MOCK-");
        if (pago.getInitPoint() == null || pago.getInitPoint().isBlank() || esSimulado) {
            Map<String, String> preferencia = mercadoPagoServicio.crearPreferencia(ordenId, pago.getMonto());
            String nuevoPrefId = preferencia.get("preferenciaId");
            if (pago.getPreferenciaId() == null || !nuevoPrefId.startsWith("MOCK-") || (esSimulado && nuevoPrefId.startsWith("MOCK-"))) {
                pago.setPreferenciaId(nuevoPrefId);
                pago.setInitPoint(preferencia.get("initPoint"));
                pagoRepositorio.save(pago);
            }
        }

        return toPreferenciaResponse(ordenId, pago);
    }

    @Transactional
    public void procesarNotificacionMercadoPago(String paymentId) {
        if (paymentId == null || paymentId.isBlank()) {
            return;
        }

        Map<String, Object> detalle = mercadoPagoServicio.consultarPago(paymentId);
        if (detalle == null) {
            log.warn("service=pago-ms paymentId={} status=payment-not-found", paymentId);
            return;
        }

        String externalReference = (String) detalle.get("external_reference");
        if (externalReference == null || externalReference.isBlank()) {
            log.warn("service=pago-ms paymentId={} status=missing-external-reference", paymentId);
            return;
        }

        Long ordenId = Long.valueOf(externalReference);
        Pago pago = pagoRepositorio.findTopByOrdenIdOrderByIdDesc(ordenId)
                .orElse(null);
        if (pago == null) {
            log.warn("service=pago-ms ordenId={} status=payment-record-not-found", ordenId);
            return;
        }

        if (ESTADO_APROBADO.equals(pago.getEstado()) || ESTADO_RECHAZADO.equals(pago.getEstado())) {
            return;
        }

        pago.setMercadopagoPaymentId(paymentId);
        String mpStatus = (String) detalle.get("status");

        if (MP_STATUS_APPROVED.equals(mpStatus)) {
            finalizarPago(pago, ESTADO_APROBADO, TIPO_EVENTO_PAGO_REALIZADO);
        } else if ("rejected".equals(mpStatus) || "cancelled".equals(mpStatus)) {
            finalizarPago(pago, ESTADO_RECHAZADO, TIPO_EVENTO_PAGO_FALLIDO);
        } else {
            log.info("service=pago-ms ordenId={} mpStatus={} status=pending", ordenId, mpStatus);
        }
    }

    @Transactional
    public PreferenciaResponse simularAprobacion(Long ordenId) {
        Pago pago = pagoRepositorio.findTopByOrdenIdOrderByIdDesc(ordenId)
                .orElseThrow(() -> new IllegalArgumentException("No existe pago para la orden " + ordenId));

        if (ESTADO_APROBADO.equals(pago.getEstado())) {
            return toPreferenciaResponse(ordenId, pago);
        }

        pago.setMercadopagoPaymentId("MOCK-" + ordenId);
        finalizarPago(pago, ESTADO_APROBADO, TIPO_EVENTO_PAGO_REALIZADO);

        return toPreferenciaResponse(ordenId, pago);
    }

    private PreferenciaResponse toPreferenciaResponse(Long ordenId, Pago pago) {
        boolean simulacion = pago.getPreferenciaId() != null && pago.getPreferenciaId().startsWith("MOCK-");
        return PreferenciaResponse.builder()
                .ordenId(ordenId)
                .preferenciaId(pago.getPreferenciaId())
                .initPoint(pago.getInitPoint())
                .estado(pago.getEstado())
                .publicKey(publicKey != null && !publicKey.isBlank() ? publicKey : null)
                .simulacion(simulacion)
                .build();
    }

    private void finalizarPago(Pago pago, String estado, String tipoEvento) {
        pago.setEstado(estado);
        pagoRepositorio.save(pago);

        EventoPago eventoPago = EventoPago.builder()
                .tipoEvento(tipoEvento)
                .ordenId(pago.getOrdenId())
                .monto(pago.getMonto())
                .estado(estado)
                .origen(applicationName)
                .timestamp(Instant.now().toEpochMilli())
                .build();

        productorPago.enviarEventoPago(eventoPago);
        log.info("service=pago-ms ordenId={} estadoPago={} status=processed", pago.getOrdenId(), estado);
    }
}
