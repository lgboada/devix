package com.devix.repository;

import com.devix.domain.CalendarioGoogleCuenta;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarioGoogleCuentaRepository extends JpaRepository<CalendarioGoogleCuenta, Long> {
    Optional<CalendarioGoogleCuenta> findByNoCiaAndUsuario_Login(Long noCia, String login);
}
