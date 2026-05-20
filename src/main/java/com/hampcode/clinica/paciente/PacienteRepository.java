package com.hampcode.clinica.paciente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio de Pacientes encargado de realizar operaciones CRUD directamente sobre PostgreSQL.
 * 
 * - @Repository: Indica a Spring que esta interfaz es un "Bean" de persistencia de datos (acceso a BD).
 *   Habilita la traducción automática de excepciones de base de datos a excepciones de Spring.
 * 
 * - JpaRepository<Paciente, Long>: Indica que el repositorio maneja la entidad "Paciente" y que 
 *   su Llave Primaria (PK) es de tipo "Long". Al heredar de JpaRepository, Spring genera automáticamente
 *   métodos CRUD básicos en tiempo de ejecución (save, findById, findAll, deleteById, existsById, etc.).
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    /**
     * Busca un paciente por su DNI.
     * 
     * Este es un "Query Method". Spring Boot lee el nombre del método `findByDni` y genera automáticamente
     * la consulta SQL: `SELECT * FROM pacientes WHERE dni = ?`.
     * Retorna un `Optional<Paciente>` porque el paciente puede o no existir en base de datos.
     */
    Optional<Paciente> findByDni(String dni);

    /**
     * Valida si ya existe algún paciente registrado con un DNI específico.
     * 
     * Spring genera la consulta: `SELECT COUNT(*) > 0 FROM pacientes WHERE dni = ?`.
     */
    boolean existsByDni(String dni);

    /**
     * Valida si ya existe algún paciente registrado con un email específico.
     * 
     * Spring genera la consulta: `SELECT COUNT(*) > 0 FROM pacientes WHERE email = ?`.
     */
    boolean existsByEmail(String email);
}
