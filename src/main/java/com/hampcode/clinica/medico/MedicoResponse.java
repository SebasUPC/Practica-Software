package com.hampcode.clinica.medico;

/**
 * Record DTO inmutable utilizado para responder con los datos limpios de un Médico.
 */
public record MedicoResponse(
    Long id,
    String nombre,
    String especialidad,
    String email
) {}
