package com.devix.web.rest;

import com.devix.security.SecurityUtils;
import com.devix.security.company.CompanyContextService;
import com.devix.service.CalendarioCitaService;
import com.devix.service.GoogleCalendarService;
import com.devix.service.dto.CalendarioCitaDTO;
import com.devix.service.dto.GoogleCalendarAuthUrlDTO;
import com.devix.service.dto.GoogleCalendarStatusDTO;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendario-citas")
public class CalendarioCitaResource {

    private static final Logger LOG = LoggerFactory.getLogger(CalendarioCitaResource.class);

    private final CalendarioCitaService calendarioCitaService;
    private final GoogleCalendarService googleCalendarService;
    private final CompanyContextService companyContextService;

    public CalendarioCitaResource(
        CalendarioCitaService calendarioCitaService,
        GoogleCalendarService googleCalendarService,
        CompanyContextService companyContextService
    ) {
        this.calendarioCitaService = calendarioCitaService;
        this.googleCalendarService = googleCalendarService;
        this.companyContextService = companyContextService;
    }

    @GetMapping("")
    public ResponseEntity<List<CalendarioCitaDTO>> getByRange(@RequestParam("from") Instant from, @RequestParam("to") Instant to) {
        Long noCia = companyContextService.getCurrentCompanyIdOrThrow();
        return ResponseEntity.ok(calendarioCitaService.findByRange(noCia, from, to));
    }

    @PostMapping("")
    public ResponseEntity<CalendarioCitaDTO> create(@Valid @RequestBody CalendarioCitaDTO dto) {
        Long noCia = companyContextService.getCurrentCompanyIdOrThrow();
        String login = currentLogin();
        CalendarioCitaDTO result = calendarioCitaService.create(noCia, login, dto);
        return ResponseEntity.created(URI.create("/api/calendario-citas/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalendarioCitaDTO> update(@PathVariable("id") Long id, @Valid @RequestBody CalendarioCitaDTO dto) {
        Long noCia = companyContextService.getCurrentCompanyIdOrThrow();
        String login = currentLogin();
        return ResponseEntity.ok(calendarioCitaService.update(noCia, login, id, dto));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<CalendarioCitaDTO> cancel(@PathVariable("id") Long id) {
        Long noCia = companyContextService.getCurrentCompanyIdOrThrow();
        String login = currentLogin();
        return ResponseEntity.ok(calendarioCitaService.cancel(noCia, login, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        Long noCia = companyContextService.getCurrentCompanyIdOrThrow();
        String login = currentLogin();
        calendarioCitaService.delete(noCia, login, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> deleteWithPost(@PathVariable("id") Long id) {
        Long noCia = companyContextService.getCurrentCompanyIdOrThrow();
        String login = currentLogin();
        calendarioCitaService.delete(noCia, login, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/google/sync")
    public ResponseEntity<Void> syncGoogle(@RequestParam("from") Instant from, @RequestParam("to") Instant to) {
        Long noCia = companyContextService.getCurrentCompanyIdOrThrow();
        String login = currentLogin();
        calendarioCitaService.syncRangeFromGoogle(noCia, login, from, to);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/google/auth-url")
    public ResponseEntity<GoogleCalendarAuthUrlDTO> getGoogleAuthUrl() {
        Long noCia = companyContextService.getCurrentCompanyIdOrThrow();
        String login = currentLogin();
        String authUrl = googleCalendarService.buildAuthorizationUrl(login, noCia);
        return ResponseEntity.ok(new GoogleCalendarAuthUrlDTO(authUrl));
    }

    @GetMapping("/google/status")
    public ResponseEntity<GoogleCalendarStatusDTO> getGoogleStatus() {
        Long noCia = companyContextService.getCurrentCompanyIdOrThrow();
        String login = currentLogin();
        return ResponseEntity.ok(googleCalendarService.getConnectionStatus(login, noCia));
    }

    @GetMapping("/google/callback")
    public ResponseEntity<Void> googleCallback(@RequestParam("code") String code, @RequestParam("state") String state) {
        LOG.debug("OAuth callback de Google Calendar recibido");
        String redirectUrl = googleCalendarService.handleOauthCallback(code, state);
        return ResponseEntity.status(302).location(URI.create(redirectUrl)).build();
    }

    private String currentLogin() {
        return SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new IllegalStateException("No se pudo identificar el usuario autenticado"));
    }
}
