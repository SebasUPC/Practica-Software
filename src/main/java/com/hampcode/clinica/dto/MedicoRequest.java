package com.hampcode.clinica.dto;

import jakarta.validation.constraints.*;

public record MedicoRequest(
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100)
    String nombre,

    @NotBlank(message = "La especialidad no puede estar vacía")
    @Size(max = 100)
    String especialidad,

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email es inválido")
    String email
) {}
