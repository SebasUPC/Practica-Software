package com.hampcode.clinica.domain;

/**
 * Enum EstadoCita que define los estados válidos por los que puede pasar una cita médica.
 * 
 * El uso de enums es una excelente práctica de Programación Orientada a Objetos en Ingeniería de Software,
 * ya que restringe los estados del negocio a valores específicos y seguros, evitando que se registren
 * estados inválidos mediante textos libres (ej. "PENDIENTEE" con error de tipeo).
 */
public enum EstadoCita {
    /**
     * Cita reservada correctamente, a la espera de ser atendida.
     */
    PENDIENTE,

    /**
     * El paciente asistió y la consulta médica se completó satisfactoriamente.
     */
    COMPLETADA,

    /**
     * La cita fue anulada por el paciente o la clínica, liberando la hora reservada.
     */
    CANCELADA
}
