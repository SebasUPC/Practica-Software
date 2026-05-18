package com.hampcode.clinica.dto;

import jakarta.validation.constraints.*;

/**
 * DTO (Data Transfer Object) representado como un record de Java para recibir los datos de registro de un Paciente.
 * 
 * ¿Por qué usamos Records en lugar de clases comunes para DTOs en el examen?
 * Los "records" se introdujeron en Java moderno (Java 14+) específicamente para modelar portadores de datos inmutables.
 * De forma automática y en una sola línea, el record autogenera:
 * - Constructor con todos los atributos.
 * - Métodos "getter" implícitos (accedidos como `.nombre()` en lugar de `.getNombre()`).
 * - Métodos `equals()`, `hashCode()` y `toString()`.
 * 
 * Anotaciones de Validación (Bean Validation):
 * Estas anotaciones se ejecutan de manera automática antes de entrar al controlador si se declara la anotación `@Valid`.
 */
public record PacienteRequest(
    /**
     * Nombre completo del paciente.
     * - @NotBlank: Asegura que el nombre no sea nulo, no esté vacío y no tenga solo espacios vacíos.
     */
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100)
    String nombre,

    /**
     * DNI del paciente.
     * - @NotBlank: Obligatorio.
     * - @Size(min=8, max=8): En el examen esto garantiza que el usuario envíe un DNI peruano válido de exactamente 8 dígitos.
     */
    @NotBlank(message = "El DNI no puede estar vacío")
    @Size(min = 8, max = 8, message = "El DNI debe tener exactamente 8 caracteres")
    String dni,

    /**
     * Correo electrónico.
     * - @Email: Regla de expresión regular implícita que valida el formato correcto del correo electrónico (ej: usuario@dominio.com).
     */
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email es inválido")
    String email
) {}
