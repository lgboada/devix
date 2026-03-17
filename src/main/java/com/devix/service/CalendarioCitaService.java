package com.devix.service;

import com.devix.domain.CalendarioCita;
import com.devix.domain.Cliente;
import com.devix.domain.User;
import com.devix.repository.CalendarioCitaRepository;
import com.devix.repository.ClienteRepository;
import com.devix.repository.UserRepository;
import com.devix.service.dto.CalendarioCitaDTO;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CalendarioCitaService {

    private static final Logger LOG = LoggerFactory.getLogger(CalendarioCitaService.class);
    private static final String ESTADO_PROGRAMADA = "PROGRAMADA";
    private static final String ESTADO_CANCELADA = "CANCELADA";
    private static final ZoneId APP_ZONE = ZoneId.of("America/Guayaquil");

    private final CalendarioCitaRepository calendarioCitaRepository;
    private final ClienteRepository clienteRepository;
    private final UserRepository userRepository;
    private final GoogleCalendarService googleCalendarService;

    public CalendarioCitaService(
        CalendarioCitaRepository calendarioCitaRepository,
        ClienteRepository clienteRepository,
        UserRepository userRepository,
        GoogleCalendarService googleCalendarService
    ) {
        this.calendarioCitaRepository = calendarioCitaRepository;
        this.clienteRepository = clienteRepository;
        this.userRepository = userRepository;
        this.googleCalendarService = googleCalendarService;
    }

    @Transactional(readOnly = true)
    public List<CalendarioCitaDTO> findByRange(Long noCia, Instant from, Instant to) {
        LocalDateTime localFrom = LocalDateTime.ofInstant(from, APP_ZONE);
        LocalDateTime localTo = LocalDateTime.ofInstant(to, APP_ZONE);
        return calendarioCitaRepository
            .findAllByNoCiaAndInicioLessThanAndFinGreaterThanOrderByInicioAsc(noCia, localTo, localFrom)
            .stream()
            .map(this::toDto)
            .toList();
    }

    public CalendarioCitaDTO create(Long noCia, String login, CalendarioCitaDTO dto) {
        validateRange(dto.getInicio(), dto.getFin());
        if (dto.getClienteId() == null) {
            throw new IllegalStateException("Debe seleccionar un cliente para agendar la cita");
        }
        CalendarioCita cita = new CalendarioCita();
        applyDtoForCreate(cita, dto);
        cita.setEstado(dto.getEstado() == null || dto.getEstado().isBlank() ? ESTADO_PROGRAMADA : dto.getEstado());
        cita.setNoCia(noCia);
        cita.setUsuario(resolveUser(login));
        cita.setGoogleSynced(Boolean.FALSE);
        cita = calendarioCitaRepository.save(cita);

        try {
            googleCalendarService.syncCitaToGoogle(cita, login);
        } catch (Exception e) {
            LOG.warn("No se pudo sincronizar cita {} con Google Calendar", cita.getId(), e);
        }
        return toDto(cita);
    }

    public CalendarioCitaDTO update(Long noCia, String login, Long id, CalendarioCitaDTO dto) {
        CalendarioCita cita = calendarioCitaRepository
            .findByIdAndNoCia(id, noCia)
            .orElseThrow(() -> new IllegalStateException("No se encontro la cita"));
        validateRange(dto.getInicio(), dto.getFin());
        applyDtoForUpdate(cita, dto);
        if (cita.getEstado() == null || cita.getEstado().isBlank()) {
            cita.setEstado(ESTADO_PROGRAMADA);
        }
        cita = calendarioCitaRepository.save(cita);

        try {
            if (ESTADO_CANCELADA.equalsIgnoreCase(cita.getEstado())) {
                googleCalendarService.deleteGoogleEvent(cita, login);
            } else {
                googleCalendarService.syncCitaToGoogle(cita, login);
            }
        } catch (Exception e) {
            LOG.warn("No se pudo sincronizar actualizacion de cita {} con Google Calendar", cita.getId(), e);
        }
        return toDto(cita);
    }

    public CalendarioCitaDTO cancel(Long noCia, String login, Long id) {
        CalendarioCita cita = calendarioCitaRepository
            .findByIdAndNoCia(id, noCia)
            .orElseThrow(() -> new IllegalStateException("No se encontro la cita"));
        cita.setEstado(ESTADO_CANCELADA);
        cita = calendarioCitaRepository.save(cita);
        try {
            googleCalendarService.deleteGoogleEvent(cita, login);
        } catch (Exception e) {
            LOG.warn("No se pudo cancelar evento {} en Google Calendar", cita.getId(), e);
        }
        return toDto(cita);
    }

    public void syncRangeFromGoogle(Long noCia, String login, Instant from, Instant to) {
        googleCalendarService.syncRangeFromGoogle(login, noCia, from, to);
    }

    public void delete(Long noCia, String login, Long id) {
        CalendarioCita cita = calendarioCitaRepository
            .findByIdAndNoCia(id, noCia)
            .orElseThrow(() -> new IllegalStateException("No se encontro la cita"));
        try {
            googleCalendarService.deleteGoogleEvent(cita, login);
        } catch (Exception e) {
            LOG.warn("No se pudo eliminar evento {} en Google Calendar", cita.getId(), e);
        }
        calendarioCitaRepository.delete(cita);
    }

    private void applyDtoForCreate(CalendarioCita cita, CalendarioCitaDTO dto) {
        cita.setTitulo(dto.getTitulo());
        cita.setDescripcion(dto.getDescripcion());
        cita.setInicio(dto.getInicio());
        cita.setFin(dto.getFin());
        if (dto.getEstado() != null) {
            cita.setEstado(dto.getEstado());
        }
        if (dto.getClienteId() != null) {
            Cliente cliente = clienteRepository
                .findById(dto.getClienteId())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));
            cita.setCliente(cliente);
        } else {
            cita.setCliente(null);
        }
    }

    private void applyDtoForUpdate(CalendarioCita cita, CalendarioCitaDTO dto) {
        if (dto.getTitulo() != null && !dto.getTitulo().isBlank()) {
            cita.setTitulo(dto.getTitulo());
        }
        if (dto.getDescripcion() != null) {
            cita.setDescripcion(dto.getDescripcion());
        }
        cita.setInicio(dto.getInicio());
        cita.setFin(dto.getFin());
        if (dto.getEstado() != null) {
            cita.setEstado(dto.getEstado());
        }
        if (dto.getClienteId() != null) {
            Cliente cliente = clienteRepository
                .findById(dto.getClienteId())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));
            cita.setCliente(cliente);
        }
    }

    private CalendarioCitaDTO toDto(CalendarioCita cita) {
        CalendarioCitaDTO dto = new CalendarioCitaDTO();
        dto.setId(cita.getId());
        dto.setNoCia(cita.getNoCia());
        dto.setTitulo(cita.getTitulo());
        dto.setDescripcion(cita.getDescripcion());
        dto.setInicio(cita.getInicio());
        dto.setFin(cita.getFin());
        dto.setEstado(cita.getEstado());
        dto.setGoogleSynced(cita.getGoogleSynced());
        dto.setGoogleEventId(cita.getGoogleEventId());
        dto.setGoogleCalendarId(cita.getGoogleCalendarId());
        dto.setClienteId(cita.getCliente() != null ? cita.getCliente().getId() : null);
        dto.setUsuarioLogin(cita.getUsuario() != null ? cita.getUsuario().getLogin() : null);
        return dto;
    }

    private User resolveUser(String login) {
        return userRepository.findOneByLogin(login).orElseThrow(() -> new IllegalStateException("No se encontro el usuario autenticado"));
    }

    private void validateRange(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null || fin == null || !fin.isAfter(inicio)) {
            throw new IllegalStateException("El rango de fecha/hora de la cita no es valido");
        }
    }
}
