package com.hampcode.clinica.cita;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * Record DTO para recibir la solicitud de reserva de una nueva Cita Médica.
 * 
 * En el examen, para programar cualquier evento asociativo, no le pidas al usuario que envíe
 * objetos complejos de Paciente o Medico. El cliente de Postman solo debe enviar los identificadores 
 * planos (`Long pacienteId`, `Long medicoId`). La capa de servicios se encargará de buscarlos en la BD.
 */
public record CitaRequest(
    /**
     * ID único del paciente que reserva la cita.
     */
    @NotNull(message = "El id del paciente es obligatorio")
    Long pacienteId,

    /**
     * ID único del médico con el cual se reserva la consulta.
     */
    @NotNull(message = "El id del médico es obligatorio")
    Long medicoId,

    /**
     * Fecha y hora seleccionada para la cita médica.
     * 
     * - @Future: **Regla de negocio implícita.** Valida automáticamente que la cita se programe en una fecha y hora 
     *   futura a la actual. Sería un error permitir registrar citas en el pasado.
     */
    @NotNull(message = "La fecha y hora son obligatorias")
    @Future(message = "La fecha y hora deben ser en el futuro")
    LocalDateTime fechaHora,

    /**
     * Motivo de la cita (ej. "Dolor abdominal continuo").
     */
    @NotBlank(message = "El motivo no puede estar vacío")
    @Size(max = 255)
    String motivo
) {}
