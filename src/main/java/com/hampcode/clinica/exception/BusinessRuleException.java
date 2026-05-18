package com.hampcode.clinica.exception;

/**
 * Excepción personalizada utilizada para reportar cualquier violación a las reglas de negocio de la clínica.
 * 
 * Por ejemplo:
 * - Intentar programar una cita en un horario ocupado.
 * - Intentar registrar a un paciente con un DNI duplicado.
 * - Intentar cancelar una cita que ya ha sido cancelada previamente.
 * 
 * Al heredar de RuntimeException, se integra perfectamente con el interceptor global de excepciones de Spring.
 */
public class BusinessRuleException extends RuntimeException {
    
    /**
     * Constructor que recibe el detalle exacto de la regla de negocio infringida.
     */
    public BusinessRuleException(String message) {
        super(message);
    }
}
