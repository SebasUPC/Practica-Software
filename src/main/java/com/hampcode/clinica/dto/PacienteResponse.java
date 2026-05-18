package com.hampcode.clinica.dto;

public record PacienteResponse(
    Long id,
    String nombre,
    String dni,
    String email
) {}
