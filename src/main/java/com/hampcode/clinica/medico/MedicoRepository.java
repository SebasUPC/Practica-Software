package com.hampcode.clinica.medico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio de Médicos encargado de la persistencia de datos.
 * 
 * Al heredar de JpaRepository<Medico, Long>, Spring Boot implementa de manera automática 
 * todos los métodos CRUD e inyecta la conexión hacia PostgreSQL.
 */
@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    /**
     * Busca médicos por su especialidad médica ignorando mayúsculas y minúsculas (ej. "cardiologia" o "CARDIOLOGÍA").
     * 
     * Es un Query Method con la palabra clave "IgnoreCase". Spring genera el SQL:
     * `SELECT * FROM medicos WHERE LOWER(especialidad) = LOWER(?)`.
     */
    List<Medico> findByEspecialidadIgnoreCase(String especialidad);

    /**
     * Valida si existe un médico con un email determinado.
     * Evita correos duplicados en el registro.
     */
    boolean existsByEmail(String email);
}
