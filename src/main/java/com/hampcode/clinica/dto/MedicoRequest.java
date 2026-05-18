package com.hampcode.clinica.dto;

import jakarta.validation.constraints.*;

/**
 * Record DTO para recibir los datos de registro de un Médico desde Postman o la vista.
 */
public record MedicoRequest(
    /**
     * Nombre completo del médico.
     */
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100)
    String nombre,

    /**
     * Especialidad médica (ej: Pediatría).
     */
    @NotBlank(message = "La especialidad no puede estar vacía")
    @Size(max = 100)
    String especialidad,

    /**
     * Correo del médico.
     * - @Email: Aplica validación automática del patrón del email.
     */
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email es inválido")
    String email
) {}
