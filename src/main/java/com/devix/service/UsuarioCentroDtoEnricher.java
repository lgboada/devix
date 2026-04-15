package com.devix.service;

import com.devix.domain.Compania;
import com.devix.repository.CompaniaRepository;
import com.devix.service.dto.UsuarioCentroDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Completa {@link UsuarioCentroDTO} con datos de solo lectura (p. ej. nombre de compañía por {@code noCia}).
 */
@Component
public class UsuarioCentroDtoEnricher {

    private final CompaniaRepository companiaRepository;

    public UsuarioCentroDtoEnricher(CompaniaRepository companiaRepository) {
        this.companiaRepository = companiaRepository;
    }

    public void enrichCompaniaNombre(UsuarioCentroDTO dto) {
        if (dto == null) {
            return;
        }
        enrichCompaniaNombre(List.of(dto));
    }

    public void enrichCompaniaNombre(List<UsuarioCentroDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }
        Set<Long> noCias = dtos.stream().map(UsuarioCentroDTO::getNoCia).filter(Objects::nonNull).collect(Collectors.toSet());
        if (noCias.isEmpty()) {
            return;
        }
        List<Compania> companias = companiaRepository.findAllByNoCiaIn(new ArrayList<>(noCias));
        Map<Long, String> nombrePorNoCia = companias
            .stream()
            .collect(Collectors.toMap(Compania::getNoCia, Compania::getNombre, (a, b) -> a));
        for (UsuarioCentroDTO dto : dtos) {
            if (dto.getNoCia() != null) {
                dto.setCompaniaNombre(nombrePorNoCia.get(dto.getNoCia()));
            }
        }
    }
}
