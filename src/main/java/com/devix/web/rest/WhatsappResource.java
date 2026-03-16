package com.devix.web.rest;

import com.devix.service.WhatsappService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsappResource {

    private static final Logger LOG = LoggerFactory.getLogger(WhatsappResource.class);

    private final WhatsappService whatsappService;
    private final ObjectMapper objectMapper;

    public WhatsappResource(WhatsappService whatsappService, ObjectMapper objectMapper) {
        this.whatsappService = whatsappService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "/send", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> sendMessage(@RequestBody Map<String, String> body) throws Exception {
        String to = body.get("to");
        String message = body.get("message");

        LOG.debug("REST request to send WhatsApp to {}", to);
        String providerResponse = whatsappService.sendTextMessage(to, message);

        return ResponseEntity.ok(Map.of("message", "WhatsApp enviado correctamente", "to", to, "providerResponse", providerResponse));
    }

    @PostMapping(value = "/send-template", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> sendTemplateMessage(@RequestBody Map<String, Object> body) throws Exception {
        String to = (String) body.get("to");
        String templateName = (String) body.get("templateName");
        String languageCode = (String) body.get("languageCode");
        List<String> bodyParameters = body.containsKey("bodyParameters")
            ? objectMapper.convertValue(body.get("bodyParameters"), new TypeReference<List<String>>() {})
            : List.of();

        LOG.debug("REST request to send WhatsApp template {} to {}", templateName, to);
        String providerResponse = whatsappService.sendTemplateMessage(to, templateName, languageCode, bodyParameters);

        return ResponseEntity.ok(
            Map.of(
                "message",
                "WhatsApp template enviado correctamente",
                "to",
                to,
                "templateName",
                templateName,
                "providerResponse",
                providerResponse
            )
        );
    }
}
