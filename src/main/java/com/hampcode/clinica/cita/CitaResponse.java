package com.hampcode.clinica.cita;

import java.time.LocalDateTime;

/**
 * Record DTO plano e inmutable para responder con el detalle completo de una Cita Médica.
 * 
 * ¿Por qué este DTO es tan poderoso en el examen?
 * En lugar de retornar los IDs secos de paciente y médico, este DTO aplana la respuesta,
 * devolviendo los nombres directamente al cliente de manera muy elegante y legible:
 * - `pacienteNombre`
 * - `medicoNombre`
 * - `medicoEspecialidad`
 * Evita la sobrecarga de datos innecesarios y reduce la cantidad de llamadas HTTP del frontend.
 */
public record CitaResponse(
    Long id,
    LocalDateTime fechaHora,
    Long pacienteId,
    String pacienteNombre,
    Long medicoId,
    String medicoNombre,
    String medicoEspecialidad,
    String motivo,
    EstadoCita estado
) {}
