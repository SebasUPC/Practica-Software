package com.hampcode.clinica.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA "Cita" que representa la tabla central "citas" en la base de datos.
 * Esta clase orquesta la relación de negocio entre Paciente y Médico.
 */
@Entity
@Table(name = "citas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cita {

    /**
     * Identificador único (Primary Key) autoincremental de la cita médica.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha y hora en la que está programada la cita médica.
     * 
     * Se mapea como TIMESTAMP en SQL.
     * - name = "fecha_hora": Cambia el formato de camelCase de Java a snake_case estándar en bases de datos relacionales.
     */
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    /**
     * Relación de muchos a uno: Muchas citas médicas pertenecen a un único Paciente.
     * 
     * - @ManyToOne: Establece la relación asociativa en el modelo relacional.
     * - fetch = FetchType.LAZY: Configura una carga perezosa (Lazy). Hibernate no traerá toda la data del paciente 
     *   de la base de datos hasta que explícitamente se invoque `.getPaciente()`. Esto optimiza la memoria y el rendimiento.
     * - @JoinColumn(name = "paciente_id", nullable = false): Mapea la columna de Llave Foránea (Foreign Key - FK) 
     *   llamada "paciente_id" en la tabla "citas", y define que no puede ser nula.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    /**
     * Relación de muchos a uno: Muchas citas médicas pueden ser atendidas por un único Médico.
     * 
     * - @ManyToOne: Mapea la relación de llave foránea.
     * - fetch = FetchType.LAZY: Carga perezosa del objeto médico por optimización.
     * - @JoinColumn(name = "medico_id", nullable = false): Columna FK llamada "medico_id" que conecta con la tabla medicos.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    /**
     * Motivo o síntomas por los cuales el paciente programa la consulta (ej. "Control de presión").
     */
    @Column(nullable = false, length = 255)
    private String motivo;

    /**
     * Estado actual de la cita (PENDIENTE, COMPLETADA, CANCELADA).
     * 
     * - @Enumerated(EnumType.STRING): **Crítico para el examen.** Por defecto, JPA guarda los enums en base de datos como números
     *   basados en su índice (0, 1, 2). Si reordenas el enum, tu base de datos se corrompe.
     *   EnumType.STRING le indica a Hibernate que guarde los textos exactos (ej. "PENDIENTE", "CANCELADA") como VARCHAR.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCita estado;
}
