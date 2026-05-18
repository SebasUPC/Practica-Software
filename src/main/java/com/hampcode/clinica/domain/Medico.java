package com.hampcode.clinica.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA "Medico" que representa la tabla "medicos" en la base de datos PostgreSQL.
 * 
 * - @Entity: Mapea la clase a una tabla en base de datos.
 * - @Table(name = "medicos"): Nombra la tabla de la base de datos en plural.
 * - Lombok (@Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, @Builder) 
 *   automatiza constructores, getters, setters y el patrón constructor Builder.
 */
@Entity
@Table(name = "medicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medico {

    /**
     * Identificador único (Primary Key) autoincremental del médico.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre completo del médico.
     * 
     * - nullable = false: Genera restricción NOT NULL en la base de datos.
     * - length = 100: Limita el texto en base de datos a VARCHAR(100).
     */
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * Especialidad médica (ej. Pediatría, Cardiología, Infectología).
     */
    @Column(nullable = false, length = 100)
    private String especialidad;

    /**
     * Correo electrónico corporativo único del médico.
     * 
     * - unique = true: Garantiza que ningún médico comparta la misma dirección de email.
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;
}
