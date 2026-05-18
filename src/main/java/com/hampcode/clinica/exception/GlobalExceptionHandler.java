package com.hampcode.clinica.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Interceptor Centralizado de Excepciones del Backend (Manejador Global).
 * 
 * - @RestControllerAdvice: Es un "aspecto" o componente interceptor de Spring. Escucha de manera global 
 *   todas las excepciones que ocurran dentro de cualquier controlador (@RestController) del proyecto.
 *   Captura el error, formatea la respuesta en un JSON estructurado y amigable, y le asigna el código de estado HTTP correcto.
 *   ¡Esto le dará un aspecto extremadamente premium a tu examen!
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. Intercepta excepciones de tipo ResourceNotFoundException (Error 404).
     * 
     * - @ExceptionHandler(ResourceNotFoundException.class): Le dice a Spring que cuando se lance esta 
     *   excepción en cualquier servicio o controlador, se ejecute este bloque de código.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value()); // HTTP 404
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * 2. Intercepta violaciones de reglas de negocio (Error 400 Bad Request).
     * 
     * Se activa cuando falla alguna validación lógica (ej: cruces de horarios).
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessRule(BusinessRuleException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value()); // HTTP 400
        body.put("error", "Business Rule Violation");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * 3. Intercepta errores de validación de inputs del cliente (Error 400 Bad Request).
     * 
     * Se activa automáticamente cuando las anotaciones de validación (como @NotBlank, @Size, @Email, @Future) 
     * fallan al recibir un RequestBody con @Valid en el controlador.
     * 
     * MethodArgumentNotValidException contiene la colección completa de campos fallidos y sus respectivos mensajes de error.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value()); // HTTP 400
        body.put("error", "Bad Request");
        
        // Extraemos cada error campo por campo y lo mapeamos en un JSON ordenado
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        body.put("validationErrors", fieldErrors);
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * 4. Intercepta cualquier otro error inesperado no controlado (Error 500 Internal Server Error).
     * 
     * Previene fallas críticas de servidor crudas y asegura que la API siempre retorne un JSON estructurado.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value()); // HTTP 500
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
