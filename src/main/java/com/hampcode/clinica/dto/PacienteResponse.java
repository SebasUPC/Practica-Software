package com.hampcode.clinica.dto;

/**
 * DTO representado como Record para retornar los datos de un Paciente al cliente.
 * 
 * ¿Por qué no devolvemos la clase Paciente.java directamente?
 * En ingeniería de software es fundamental el desacoplamiento: las clases @Entity tienen dependencias 
 * con la base de datos (y Hibernate). Si devuelves la entidad directamente, podrías incurrir en problemas de:
 * 1. LazyInitializationException al intentar serializar relaciones pesadas.
 * 2. Exposición accidental de datos sensibles.
 * 3. Enviar estructuras cíclicas e infinitas de JSON.
 * El Response DTO contiene únicamente los datos limpios de lectura en un formato plano.
 */
public record PacienteResponse(
    Long id,
    String nombre,
    String dni,
    String email
) {}
