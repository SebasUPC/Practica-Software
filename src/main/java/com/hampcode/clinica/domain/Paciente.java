package com.hampcode.clinica.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA "Paciente" que representa la tabla "pacientes" en la base de datos PostgreSQL.
 * 
 * Explicación de anotaciones de nivel de clase:
 * - @Entity: Registra esta clase con Hibernate para que sea mapeada como una tabla en base de datos.
 * - @Table(name = "pacientes"): Asigna un nombre personalizado a la tabla en SQL (plural en minúsculas).
 * - @Getter y @Setter (Lombok): Genera todos los métodos get y set de forma automática al compilar.
 * - @NoArgsConstructor (Lombok): Genera el constructor vacío obligatorio por el estándar JPA/Hibernate.
 * - @AllArgsConstructor (Lombok): Genera el constructor con todos los atributos para flexibilidad.
 * - @Builder (Lombok): Habilita el patrón Builder, permitiendo construir objetos como: Paciente.builder().nombre("x").build().
 */
@Entity
@Table(name = "pacientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {

    /**
     * Llave primaria (Primary Key) autoincremental de la tabla de pacientes.
     * 
     * - @Id: Declara el atributo id como la PK de la tabla.
     * - @GeneratedValue(strategy = GenerationType.IDENTITY): Configura el autoincremento (tipo SERIAL en PostgreSQL).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre completo del paciente.
     * 
     * - @Column: Permite personalizar los constraints a nivel de base de datos.
     * - nullable = false: Genera una restricción NOT NULL en la base de datos.
     * - length = 100: Limita el tipo de dato SQL a VARCHAR(100).
     */
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * DNI único de 8 caracteres numéricos para identificación del paciente.
     * 
     * - unique = true: Garantiza que no existan dos filas con el mismo DNI (genera un UNIQUE index en SQL).
     * - length = 8: Limita la columna a exactamente VARCHAR(8).
     */
    @Column(nullable = false, unique = true, length = 8)
    private String dni;

    /**
     * Correo electrónico único del paciente.
     * 
     * - unique = true: Previene que múltiples cuentas de pacientes compartan el mismo email.
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;
}
