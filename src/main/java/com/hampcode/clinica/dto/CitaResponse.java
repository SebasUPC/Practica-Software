package com.hampcode.clinica.dto;

import com.hampcode.clinica.domain.EstadoCita;
import java.time.LocalDateTime;

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
