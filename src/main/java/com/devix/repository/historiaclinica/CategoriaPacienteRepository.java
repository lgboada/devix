package com.devix.repository.historiaclinica;

import com.devix.domain.historiaclinica.CategoriaPaciente;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface CategoriaPacienteRepository extends JpaRepository<CategoriaPaciente, Long>, JpaSpecificationExecutor<CategoriaPaciente> {}
