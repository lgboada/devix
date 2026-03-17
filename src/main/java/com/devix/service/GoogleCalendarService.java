package com.devix.service;

import com.devix.domain.CalendarioCita;
import com.devix.domain.CalendarioGoogleCuenta;
import com.devix.domain.User;
import com.devix.repository.CalendarioCitaRepository;
import com.devix.repository.CalendarioGoogleCuentaRepository;
import com.devix.repository.UserRepository;
import com.devix.service.dto.GoogleCalendarStatusDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Transactional
public class GoogleCalendarService {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleCalendarService.class);

    private static final String DEFAULT_CALENDAR_ID = "primary";
    private static final String GOOGLE_AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String GOOGLE_TOKEN_ENDPOINT = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_CALENDAR_API = "https://www.googleapis.com/calendar/v3";
    private static final String GOOGLE_SCOPE = "https://www.googleapis.com/auth/calendar";
    private static final ZoneId APP_ZONE = ZoneId.of("America/Guayaquil");

    private final CalendarioGoogleCuentaRepository calendarioGoogleCuentaRepository;
    private final CalendarioCitaRepository calendarioCitaRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    private final Map<String, OAuthState> pendingStates = new ConcurrentHashMap<>();

    @Value("${google.calendar.client-id:}")
    private String googleClientId;

    @Value("${google.calendar.client-secret:}")
    private String googleClientSecret;

    @Value("${google.calendar.redirect-uri:http://localhost:8080/api/calendario-citas/google/callback}")
    private String googleRedirectUri;

    @Value("${google.calendar.success-redirect:http://localhost:9000/calendario}")
    private String googleSuccessRedirect;

    public GoogleCalendarService(
        CalendarioGoogleCuentaRepository calendarioGoogleCuentaRepository,
        CalendarioCitaRepository calendarioCitaRepository,
        UserRepository userRepository,
        ObjectMapper objectMapper
    ) {
        this.calendarioGoogleCuentaRepository = calendarioGoogleCuentaRepository;
        this.calendarioCitaRepository = calendarioCitaRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder().build();
    }

    public String buildAuthorizationUrl(String login, Long noCia) {
        ensureGoogleConfig();
        String state = UUID.randomUUID().toString();
        pendingStates.put(state, new OAuthState(login, noCia, Instant.now().plusSeconds(600)));
        cleanupStates();

        return UriComponentsBuilder.fromHttpUrl(GOOGLE_AUTH_ENDPOINT)
            .queryParam("response_type", "code")
            .queryParam("client_id", googleClientId)
            .queryParam("redirect_uri", googleRedirectUri)
            .queryParam("scope", GOOGLE_SCOPE)
            .queryParam("access_type", "offline")
            .queryParam("prompt", "consent")
            .queryParam("state", state)
            .build(true)
            .toUriString();
    }

    public String handleOauthCallback(String code, String state) {
        ensureGoogleConfig();
        OAuthState oauthState = Optional.ofNullable(pendingStates.remove(state)).orElseThrow(() ->
            new IllegalStateException("Estado OAuth invalido")
        );
        if (oauthState.expiresAt().isBefore(Instant.now())) {
            throw new IllegalStateException("El estado OAuth expiro");
        }

        User user = userRepository.findOneByLogin(oauthState.login()).orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        JsonNode tokenNode = exchangeCodeForToken(code);
        saveOrUpdateToken(user, oauthState.noCia(), tokenNode);
        return googleSuccessRedirect + "?googleConnected=true";
    }

    @Transactional(readOnly = true)
    public GoogleCalendarStatusDTO getConnectionStatus(String login, Long noCia) {
        return calendarioGoogleCuentaRepository
            .findByNoCiaAndUsuario_Login(noCia, login)
            .map(c -> new GoogleCalendarStatusDTO(Boolean.TRUE.equals(c.getActivo()), c.getGoogleEmail()))
            .orElseGet(() -> new GoogleCalendarStatusDTO(false, null));
    }

    public Optional<CalendarioGoogleCuenta> getCuenta(String login, Long noCia) {
        return calendarioGoogleCuentaRepository.findByNoCiaAndUsuario_Login(noCia, login).filter(c -> Boolean.TRUE.equals(c.getActivo()));
    }

    public void syncCitaToGoogle(CalendarioCita cita, String login) {
        Optional<CalendarioGoogleCuenta> cuentaOpt = getCuenta(login, cita.getNoCia());
        if (cuentaOpt.isEmpty() || "CANCELADA".equalsIgnoreCase(cita.getEstado())) {
            return;
        }

        CalendarioGoogleCuenta cuenta = ensureValidAccessToken(cuentaOpt.get());
        if (cita.getGoogleEventId() == null || cita.getGoogleEventId().isBlank()) {
            String eventId = createGoogleEvent(cuenta, cita);
            cita.setGoogleEventId(eventId);
            cita.setGoogleCalendarId(cuenta.getCalendarId());
        } else {
            updateGoogleEvent(cuenta, cita);
        }
        cita.setGoogleSynced(Boolean.TRUE);
        calendarioCitaRepository.save(cita);
    }

    public void deleteGoogleEvent(CalendarioCita cita, String login) {
        if (cita.getGoogleEventId() == null || cita.getGoogleEventId().isBlank()) {
            return;
        }
        Optional<CalendarioGoogleCuenta> cuentaOpt = getCuenta(login, cita.getNoCia());
        if (cuentaOpt.isEmpty()) {
            return;
        }
        CalendarioGoogleCuenta cuenta = ensureValidAccessToken(cuentaOpt.get());
        String url =
            GOOGLE_CALENDAR_API +
            "/calendars/" +
            encodeSegment(cuenta.getCalendarId()) +
            "/events/" +
            encodeSegment(cita.getGoogleEventId());
        restClient.delete().uri(URI.create(url)).header("Authorization", "Bearer " + cuenta.getAccessToken()).retrieve().toBodilessEntity();
    }

    public void syncRangeFromGoogle(String login, Long noCia, Instant from, Instant to) {
        CalendarioGoogleCuenta cuenta = getCuenta(login, noCia)
            .map(this::ensureValidAccessToken)
            .orElseThrow(() -> new IllegalStateException("No existe una cuenta de Google conectada para este usuario"));

        String url =
            GOOGLE_CALENDAR_API +
            "/calendars/" +
            encodeSegment(cuenta.getCalendarId()) +
            "/events?" +
            "singleEvents=true&orderBy=startTime" +
            "&timeMin=" +
            URLEncoder.encode(from.toString(), StandardCharsets.UTF_8) +
            "&timeMax=" +
            URLEncoder.encode(to.toString(), StandardCharsets.UTF_8);

        String body = restClient
            .get()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + cuenta.getAccessToken())
            .retrieve()
            .body(String.class);
        try {
            JsonNode root = objectMapper.readTree(body);
            JsonNode items = root.path("items");
            if (!items.isArray()) {
                return;
            }

            for (JsonNode item : items) {
                String status = item.path("status").asText();
                if ("cancelled".equalsIgnoreCase(status)) {
                    continue;
                }
                String googleEventId = item.path("id").asText(null);
                if (googleEventId == null || googleEventId.isBlank()) {
                    continue;
                }
                LocalDateTime start = parseGoogleDate(item.path("start"));
                LocalDateTime end = parseGoogleDate(item.path("end"));
                if (start == null || end == null) {
                    continue;
                }

                CalendarioCita cita = calendarioCitaRepository
                    .findByNoCiaAndGoogleEventId(noCia, googleEventId)
                    .orElseGet(CalendarioCita::new);
                cita.setNoCia(noCia);
                cita.setTitulo(item.path("summary").asText("Cita"));
                cita.setDescripcion(item.path("description").asText(null));
                cita.setInicio(start);
                cita.setFin(end);
                cita.setEstado("PROGRAMADA");
                cita.setGoogleEventId(googleEventId);
                cita.setGoogleCalendarId(cuenta.getCalendarId());
                cita.setGoogleSynced(Boolean.TRUE);
                calendarioCitaRepository.save(cita);
            }
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo sincronizar el calendario de Google", e);
        }
    }

    private JsonNode exchangeCodeForToken(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("client_id", googleClientId);
        form.add("client_secret", googleClientSecret);
        form.add("redirect_uri", googleRedirectUri);
        form.add("grant_type", "authorization_code");

        String tokenResponse = restClient
            .post()
            .uri(URI.create(GOOGLE_TOKEN_ENDPOINT))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(form)
            .retrieve()
            .body(String.class);
        try {
            return objectMapper.readTree(tokenResponse);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo leer la respuesta OAuth de Google", e);
        }
    }

    private void saveOrUpdateToken(User user, Long noCia, JsonNode tokenNode) {
        String accessToken = tokenNode.path("access_token").asText(null);
        String refreshToken = tokenNode.path("refresh_token").asText(null);
        long expiresIn = tokenNode.path("expires_in").asLong(3600L);
        if (accessToken == null || refreshToken == null) {
            throw new IllegalStateException("Google no devolvio access_token/refresh_token");
        }

        CalendarioGoogleCuenta cuenta = calendarioGoogleCuentaRepository
            .findByNoCiaAndUsuario_Login(noCia, user.getLogin())
            .orElseGet(CalendarioGoogleCuenta::new);

        String email = resolveGoogleEmail(accessToken);
        cuenta.setNoCia(noCia);
        cuenta.setUsuario(user);
        cuenta.setAccessToken(accessToken);
        cuenta.setRefreshToken(refreshToken);
        cuenta.setTokenExpiry(Instant.now().plusSeconds(expiresIn - 60));
        cuenta.setCalendarId(DEFAULT_CALENDAR_ID);
        cuenta.setActivo(Boolean.TRUE);
        cuenta.setGoogleEmail(email);
        calendarioGoogleCuentaRepository.save(cuenta);
    }

    private String resolveGoogleEmail(String accessToken) {
        try {
            String body = restClient
                .get()
                .uri(URI.create("https://www.googleapis.com/oauth2/v2/userinfo"))
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(String.class);
            JsonNode node = objectMapper.readTree(body);
            return node.path("email").asText(null);
        } catch (Exception e) {
            LOG.warn("No se pudo resolver el email de Google", e);
            return null;
        }
    }

    private CalendarioGoogleCuenta ensureValidAccessToken(CalendarioGoogleCuenta cuenta) {
        if (cuenta.getTokenExpiry() != null && cuenta.getTokenExpiry().isAfter(Instant.now().plusSeconds(30))) {
            return cuenta;
        }

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", googleClientId);
        form.add("client_secret", googleClientSecret);
        form.add("refresh_token", cuenta.getRefreshToken());
        form.add("grant_type", "refresh_token");
        String tokenResponse = restClient
            .post()
            .uri(URI.create(GOOGLE_TOKEN_ENDPOINT))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(form)
            .retrieve()
            .body(String.class);

        try {
            JsonNode node = objectMapper.readTree(tokenResponse);
            String accessToken = node.path("access_token").asText(null);
            long expiresIn = node.path("expires_in").asLong(3600L);
            if (accessToken == null) {
                throw new IllegalStateException("No se pudo renovar el token de Google");
            }
            cuenta.setAccessToken(accessToken);
            cuenta.setTokenExpiry(Instant.now().plusSeconds(expiresIn - 60));
            return calendarioGoogleCuentaRepository.save(cuenta);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo procesar el refresh token de Google", e);
        }
    }

    private String createGoogleEvent(CalendarioGoogleCuenta cuenta, CalendarioCita cita) {
        String url = GOOGLE_CALENDAR_API + "/calendars/" + encodeSegment(cuenta.getCalendarId()) + "/events";
        String payload = buildGoogleEventPayload(cita);
        String body = restClient
            .post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + cuenta.getAccessToken())
            .body(payload)
            .retrieve()
            .body(String.class);
        try {
            return objectMapper.readTree(body).path("id").asText();
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo crear evento en Google Calendar", e);
        }
    }

    private void updateGoogleEvent(CalendarioGoogleCuenta cuenta, CalendarioCita cita) {
        String url =
            GOOGLE_CALENDAR_API +
            "/calendars/" +
            encodeSegment(cuenta.getCalendarId()) +
            "/events/" +
            encodeSegment(cita.getGoogleEventId());
        String payload = buildGoogleEventPayload(cita);
        restClient
            .put()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + cuenta.getAccessToken())
            .body(payload)
            .retrieve()
            .toBodilessEntity();
    }

    private String buildGoogleEventPayload(CalendarioCita cita) {
        try {
            Map<String, Object> payload = Map.of(
                "summary",
                cita.getTitulo(),
                "description",
                cita.getDescripcion() == null ? "" : cita.getDescripcion(),
                "start",
                Map.of("dateTime", toGoogleDateTime(cita.getInicio()), "timeZone", APP_ZONE.getId()),
                "end",
                Map.of("dateTime", toGoogleDateTime(cita.getFin()), "timeZone", APP_ZONE.getId())
            );
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo serializar el evento para Google Calendar", e);
        }
    }

    private LocalDateTime parseGoogleDate(JsonNode dateNode) {
        if (dateNode == null || dateNode.isMissingNode()) {
            return null;
        }
        String dateTime = dateNode.path("dateTime").asText(null);
        if (dateTime != null && !dateTime.isBlank()) {
            return OffsetDateTime.parse(dateTime).atZoneSameInstant(APP_ZONE).toLocalDateTime();
        }
        String date = dateNode.path("date").asText(null);
        if (date != null && !date.isBlank()) {
            return LocalDate.parse(date).atStartOfDay();
        }
        return null;
    }

    private String toGoogleDateTime(LocalDateTime localDateTime) {
        return localDateTime.atZone(APP_ZONE).toOffsetDateTime().toString();
    }

    private void ensureGoogleConfig() {
        if (googleClientId == null || googleClientId.isBlank() || googleClientSecret == null || googleClientSecret.isBlank()) {
            throw new IllegalStateException("Falta configurar google.calendar.client-id/client-secret");
        }
    }

    private void cleanupStates() {
        Instant now = Instant.now();
        pendingStates.entrySet().removeIf(e -> e.getValue().expiresAt().isBefore(now));
    }

    private String encodeSegment(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private record OAuthState(String login, Long noCia, Instant expiresAt) {}
}
