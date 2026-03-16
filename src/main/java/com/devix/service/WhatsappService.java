package com.devix.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class WhatsappService {

    private static final Logger LOG = LoggerFactory.getLogger(WhatsappService.class);

    private final Environment environment;
    private final ObjectMapper objectMapper;

    public WhatsappService(Environment environment, ObjectMapper objectMapper) {
        this.environment = environment;
        this.objectMapper = objectMapper;
    }

    public String sendTextMessage(String to, String message) throws Exception {
        if (!StringUtils.hasText(to)) {
            throw new IllegalArgumentException("El parametro 'to' es obligatorio");
        }
        if (!StringUtils.hasText(message)) {
            throw new IllegalArgumentException("El parametro 'message' es obligatorio");
        }

        Map<String, Object> payload = Map.of(
            "messaging_product",
            "whatsapp",
            "to",
            to.trim(),
            "type",
            "text",
            "text",
            Map.of("body", message)
        );

        return sendPayload(to, payload);
    }

    public String sendTemplateMessage(String to, String templateName, String languageCode, List<String> bodyParameters) throws Exception {
        if (!StringUtils.hasText(to)) {
            throw new IllegalArgumentException("El parametro 'to' es obligatorio");
        }
        if (!StringUtils.hasText(templateName)) {
            throw new IllegalArgumentException("El parametro 'templateName' es obligatorio");
        }

        String templateLanguage = StringUtils.hasText(languageCode)
            ? languageCode.trim()
            : environment.getProperty("whatsapp.default-language-code", "es");

        List<Map<String, String>> parameters = new ArrayList<>();
        if (bodyParameters != null) {
            for (String value : bodyParameters) {
                parameters.add(Map.of("type", "text", "text", value == null ? "" : value));
            }
        }

        Map<String, Object> template = parameters.isEmpty()
            ? Map.of("name", templateName.trim(), "language", Map.of("code", templateLanguage))
            : Map.of(
                "name",
                templateName.trim(),
                "language",
                Map.of("code", templateLanguage),
                "components",
                List.of(Map.of("type", "body", "parameters", parameters))
            );

        Map<String, Object> payload = Map.of("messaging_product", "whatsapp", "to", to.trim(), "type", "template", "template", template);
        return sendPayload(to, payload);
    }

    private String sendPayload(String to, Map<String, Object> payload) throws Exception {
        boolean enabled = environment.getProperty("whatsapp.enabled", Boolean.class, false);
        if (!enabled) {
            throw new IllegalStateException("Whatsapp esta deshabilitado. Configure whatsapp.enabled=true");
        }

        String apiUrl = environment.getProperty("whatsapp.api-url", "https://graph.facebook.com");
        String apiVersion = environment.getProperty("whatsapp.api-version", "v21.0");
        String phoneNumberId = environment.getProperty("whatsapp.phone-number-id");
        String accessToken = environment.getProperty("whatsapp.access-token");
        Integer timeoutSeconds = environment.getProperty("whatsapp.timeout-seconds", Integer.class, 10);

        if (!StringUtils.hasText(phoneNumberId)) {
            throw new IllegalStateException("No esta configurado whatsapp.phone-number-id");
        }
        if (!StringUtils.hasText(accessToken)) {
            throw new IllegalStateException("No esta configurado whatsapp.access-token");
        }

        String endpoint = String.format("%s/%s/%s/messages", apiUrl, apiVersion, phoneNumberId);
        String requestBody = objectMapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(endpoint))
            .timeout(Duration.ofSeconds(timeoutSeconds))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + accessToken)
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(timeoutSeconds)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            LOG.error("Error enviando WhatsApp. status={}, body={}", response.statusCode(), response.body());
            throw new IllegalStateException("Error enviando WhatsApp. status=" + response.statusCode() + ", body=" + response.body());
        }

        LOG.debug("WhatsApp enviado correctamente a {}", to);
        return response.body();
    }
}
