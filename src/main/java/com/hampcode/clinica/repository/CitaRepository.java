package com.hampcode.clinica.repository;

import com.hampcode.clinica.domain.Cita;
import com.hampcode.clinica.domain.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByPacienteId(Long pacienteId);
    List<Cita> findByMedicoId(Long medicoId);

    // RN-01: Validar si un paciente ya tiene cita a esa misma hora (que no esté cancelada)
    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE c.paciente.id = :pacienteId AND c.fechaHora = :fechaHora AND c.estado != com.hampcode.clinica.domain.EstadoCita.CANCELADA")
    boolean existeCitaPacienteMismaHora(
        @Param("pacienteId") Long pacienteId, 
        @Param("fechaHora") LocalDateTime fechaHora
    );

    // RN-02: Validar si un médico ya tiene cita a esa misma hora (que no esté cancelada)
    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE c.medico.id = :medicoId AND c.fechaHora = :fechaHora AND c.estado != com.hampcode.clinica.domain.EstadoCita.CANCELADA")
    boolean existeCitaMedicoMismaHora(
        @Param("medicoId") Long medicoId, 
        @Param("fechaHora") LocalDateTime fechaHora
    );
}
