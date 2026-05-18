package com.hampcode.clinica.exception;

/**
 * Excepción personalizada para representar errores cuando un recurso no es encontrado en base de datos.
 * 
 * Hereda de `RuntimeException` (un-checked exception). Esto permite que el flujo de Java 
 * se detenga y la excepción suba de nivel de forma automática sin obligar a los métodos a declarar 
 * `throws ResourceNotFoundException` en sus firmas, manteniendo un código limpio y legible.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Constructor que recibe el mensaje descriptivo del error (ej. "Paciente no encontrado con id: X").
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
