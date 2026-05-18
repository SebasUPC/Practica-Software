package com.hampcode.clinica.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record CitaRequest(
    @NotNull(message = "El id del paciente es obligatorio")
    Long pacienteId,

    @NotNull(message = "El id del médico es obligatorio")
    Long medicoId,

    @NotNull(message = "La fecha y hora son obligatorias")
    @Future(message = "La fecha y hora deben ser en el futuro")
    LocalDateTime fechaHora,

    @NotBlank(message = "El motivo no puede estar vacío")
    @Size(max = 255)
    String motivo
) {}
