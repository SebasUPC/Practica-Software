package com.hampcode.clinica.dto;

public record MedicoResponse(
    Long id,
    String nombre,
    String especialidad,
    String email
) {}
