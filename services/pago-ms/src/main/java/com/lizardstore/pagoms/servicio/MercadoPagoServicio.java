package com.lizardstore.pagoms.servicio;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MercadoPagoServicio {

    @Value("${mercadopago.access-token:}")
    private String accessToken;

    @Value("${mercadopago.webhook-url:}")
    private String webhookUrl;

    @Value("${mercadopago.frontend-url:http://localhost:4200}")
    private String frontendUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void init() {
        if (accessToken == null || accessToken.isBlank()) {
            log.warn("service=pago-ms component=mercadopago-sdk status=missing-access-token simulation-mode=true");
            return;
        }

        log.info("service=pago-ms component=mercadopago-sdk status=initializing");
        MercadoPagoConfig.setAccessToken(accessToken);
    }

    public Map<String, String> crearPreferencia(Long ordenId, Double monto) {
        if (accessToken == null || accessToken.isBlank()) {
            return crearPreferenciaSimulada(ordenId);
        }

        try {
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id("orden-" + ordenId)
                    .title("Compra en Lizard Store - Orden #" + ordenId)
                    .quantity(1)
                    .unitPrice(BigDecimal.valueOf(monto))
                    .currencyId("PEN")
                    .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            String baseOrdenesUrl = frontendUrl + "/ordenes";
            PreferenceBackUrlsRequest backUrlsRequest = PreferenceBackUrlsRequest.builder()
                    .success(baseOrdenesUrl + "?payment_status=success&ordenId=" + ordenId)
                    .failure(baseOrdenesUrl + "?payment_status=failure&ordenId=" + ordenId)
                    .pending(baseOrdenesUrl + "?payment_status=pending&ordenId=" + ordenId)
                    .build();

            String finalWebhookUrl = null;
            if (webhookUrl != null && !webhookUrl.isBlank()
                    && !webhookUrl.contains("localhost")
                    && !webhookUrl.contains("127.0.0.1")) {
                finalWebhookUrl = webhookUrl;
            }

            com.mercadopago.client.preference.PreferenceRequest.PreferenceRequestBuilder preferenceBuilder = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrlsRequest)
                    .notificationUrl(finalWebhookUrl)
                    .externalReference(String.valueOf(ordenId));

            if (baseOrdenesUrl.startsWith("https")) {
                preferenceBuilder.autoReturn("approved");
            }

            PreferenceRequest preferenceRequest = preferenceBuilder.build();

            log.info("service=pago-ms component=mercadopago-sdk action=crear-preferencia ordenId={} webhookUrl={}",
                    ordenId, finalWebhookUrl);

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            if (preference != null && preference.getId() != null) {
                Map<String, String> result = new HashMap<>();
                result.put("preferenciaId", preference.getId());
                result.put("initPoint", preference.getSandboxInitPoint() != null
                        ? preference.getSandboxInitPoint()
                        : preference.getInitPoint());
                return result;
            }
        } catch (MPApiException apiEx) {
            log.error("service=pago-ms component=mercadopago-sdk action=crear-preferencia status=failed-api error=\"{}\" statusCode={} response=\"{}\"",
                    apiEx.getMessage(), apiEx.getStatusCode(), apiEx.getApiResponse() != null ? apiEx.getApiResponse().getContent() : null);
        } catch (Exception e) {
            log.error("service=pago-ms component=mercadopago-sdk action=crear-preferencia status=failed error=\"{}\"",
                    e.getMessage(), e);
        }

        return crearPreferenciaSimulada(ordenId);
    }

    public Map<String, Object> consultarPago(String paymentId) {
        if (accessToken == null || accessToken.isBlank()) {
            return null;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            String url = "https://api.mercadopago.com/v1/payments/" + paymentId;
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> paymentMap = response.getBody();
                log.info("service=pago-ms component=mercadopago-sdk action=consultar-pago paymentId={} status=success",
                        paymentId);

                Map<String, Object> result = new HashMap<>();
                result.put("status", paymentMap.get("status"));
                result.put("external_reference", paymentMap.get("external_reference"));
                result.put("transaction_amount", paymentMap.get("transaction_amount"));
                return result;
            }
        } catch (Exception e) {
            log.error(
                    "service=pago-ms component=mercadopago-sdk action=consultar-pago paymentId={} status=failed error=\"{}\"",
                    paymentId, e.getMessage());
        }
        return null;
    }

    public Map<String, Object> buscarPagoPorOrdenId(Long ordenId) {
        if (accessToken == null || accessToken.isBlank()) {
            return null;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            String url = "https://api.mercadopago.com/v1/payments/search?external_reference=" + ordenId + "&limit=10&offset=0";
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("results");

                if (results != null && !results.isEmpty()) {
                    // Select approved payment first, or fallback to the first element
                    Map<String, Object> payment = results.stream()
                            .filter(p -> "approved".equals(p.get("status")))
                            .findFirst()
                            .orElse(results.get(0));

                    log.info("service=pago-ms component=mercadopago-sdk action=buscar-pago-por-orden-id ordenId={} status=found paymentId={} paymentStatus={}",
                            ordenId, payment.get("id"), payment.get("status"));

                    Map<String, Object> result = new HashMap<>();
                    result.put("status", payment.get("status"));
                    result.put("id", String.valueOf(payment.get("id")));
                    result.put("external_reference", payment.get("external_reference"));
                    result.put("transaction_amount", payment.get("transaction_amount"));
                    return result;
                }
            }
        } catch (Exception e) {
            log.error(
                    "service=pago-ms component=mercadopago-sdk action=buscar-pago-por-orden-id ordenId={} status=failed error=\"{}\"",
                    ordenId, e.getMessage());
        }
        return null;
    }

    private Map<String, String> crearPreferenciaSimulada(Long ordenId) {
        Map<String, String> fallbackResult = new HashMap<>();
        fallbackResult.put("preferenciaId", "MOCK-PREFERENCE-" + ordenId);
        fallbackResult.put("initPoint",
                frontendUrl + "/ordenes?payment_status=mock-success&ordenId=" + ordenId);
        return fallbackResult;
    }
}
