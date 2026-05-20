package com.hampcode.clinica.cita;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio de Citas Médicas encargado de interactuar con la tabla "citas".
 * Contiene consultas especializadas en JPQL para verificar la disponibilidad horaria.
 */
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    /**
     * Lista todas las citas asociadas a un Paciente.
     * 
     * Spring infiere la consulta: `SELECT * FROM citas WHERE paciente_id = ?`.
     */
    List<Cita> findByPacienteId(Long pacienteId);

    /**
     * Lista todas las citas asociadas a un Médico.
     * 
     * Spring infiere la consulta: `SELECT * FROM citas WHERE medico_id = ?`.
     */
    List<Cita> findByMedicoId(Long medicoId);

    /**
     * REGLA DE NEGOCIO 1: Valida si un paciente ya tiene una cita ocupada a una misma fecha y hora.
     * 
     * ¿Por qué usamos @Query con JPQL?
     * JPQL (Java Persistence Query Language) opera sobre las clases de Java y sus relaciones en vez de las tablas SQL crudas.
     * - `c.paciente.id` navega automáticamente por la relación ManyToOne de Cita hacia la clase Paciente.
     * - `c.estado != com.hampcode.clinica.domain.EstadoCita.CANCELADA` nos asegura que si la cita previa 
     *   fue CANCELADA, ese horario se considere nuevamente LIBRE para que el paciente pueda reservar otra.
     * 
     * @Param("pacienteId") mapea el parámetro del método Java a `:pacienteId` dentro de la consulta JPQL.
     */
    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE c.paciente.id = :pacienteId AND c.fechaHora = :fechaHora AND c.estado != com.hampcode.clinica.domain.EstadoCita.CANCELADA")
    boolean existeCitaPacienteMismaHora(
        @Param("pacienteId") Long pacienteId, 
        @Param("fechaHora") LocalDateTime fechaHora
    );

    /**
     * REGLA DE NEGOCIO 2: Valida si un médico ya tiene ocupada la hora seleccionada con otra cita.
     * 
     * Funciona de forma análoga a la regla de paciente. Si el médico tiene una cita activa (PENDIENTE o COMPLETADA) 
     * en esa fecha y hora, no puede ser reservado por otro paciente. Si la cita fue CANCELADA, la hora se libera.
     */
    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE c.medico.id = :medicoId AND c.fechaHora = :fechaHora AND c.estado != com.hampcode.clinica.domain.EstadoCita.CANCELADA")
    boolean existeCitaMedicoMismaHora(
        @Param("medicoId") Long medicoId, 
        @Param("fechaHora") LocalDateTime fechaHora
    );
}
