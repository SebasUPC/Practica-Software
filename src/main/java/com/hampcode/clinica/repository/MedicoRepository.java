package com.hampcode.clinica.repository;

import com.hampcode.clinica.domain.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    List<Medico> findByEspecialidadIgnoreCase(String especialidad);
    boolean existsByEmail(String email);
}
