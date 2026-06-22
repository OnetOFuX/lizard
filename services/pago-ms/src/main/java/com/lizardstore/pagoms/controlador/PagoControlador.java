package com.lizardstore.pagoms.controlador;

import com.lizardstore.pagoms.dto.PreferenciaResponse;
import com.lizardstore.pagoms.dto.WebhookNotificacion;
import com.lizardstore.pagoms.entidad.Pago;
import com.lizardstore.pagoms.servicio.PagoServicio;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
public class PagoControlador {

    private final PagoServicio pagoServicio;

    @GetMapping("/saludo")
    public String saludo() {
        return "pago-ms activo";
    }

    @GetMapping
    public List<Pago> listarPagos() {
        return pagoServicio.listarPagos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPagoPorId(@PathVariable Long id) {
        return pagoServicio.buscarPagoPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<Pago> buscarPagoPorOrden(@PathVariable Long ordenId) {
        return pagoServicio.buscarPagoPorOrdenId(ordenId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/orden/{ordenId}/preferencia")
    public ResponseEntity<PreferenciaResponse> obtenerPreferencia(
            @PathVariable Long ordenId,
            @RequestParam(value = "monto", required = false) Double monto) {
        try {
            return ResponseEntity.ok(pagoServicio.obtenerPreferencia(ordenId, monto));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/orden/{ordenId}/simular-aprobacion")
    public ResponseEntity<PreferenciaResponse> simularAprobacion(@PathVariable Long ordenId) {
        try {
            return ResponseEntity.ok(pagoServicio.simularAprobacion(ordenId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhookMercadoPago(
            @RequestBody(required = false) WebhookNotificacion notificacion,
            @RequestParam(value = "topic", required = false) String topic,
            @RequestParam(value = "id", required = false) String id) {

        String paymentId = null;
        if (notificacion != null && notificacion.getData() != null && notificacion.getData().getId() != null) {
            paymentId = notificacion.getData().getId();
        } else if ("payment".equals(topic) && id != null) {
            paymentId = id;
        }

        if (paymentId != null) {
            log.info("service=pago-ms action=webhook paymentId={} status=received", paymentId);
            pagoServicio.procesarNotificacionMercadoPago(paymentId);
        }

        return ResponseEntity.ok().build();
    }
}
