package com.devix.repository;

import com.devix.domain.CalendarioCita;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarioCitaRepository extends JpaRepository<CalendarioCita, Long> {
    List<CalendarioCita> findAllByNoCiaAndInicioLessThanAndFinGreaterThanOrderByInicioAsc(Long noCia, LocalDateTime to, LocalDateTime from);

    Optional<CalendarioCita> findByIdAndNoCia(Long id, Long noCia);

    Optional<CalendarioCita> findByNoCiaAndGoogleEventId(Long noCia, String googleEventId);
}
