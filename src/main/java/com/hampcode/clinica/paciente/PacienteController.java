package com.hampcode.clinica.paciente;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para gestionar Pacientes.
 * 
 * - @RestController: Indica a Spring que esta clase es un controlador REST. Spring serializa de forma 
 *   automática las respuestas de los métodos (objetos Java) a formato JSON de forma nativa.
 * - @RequestMapping("/api/v1/pacientes"): Establece la ruta URL base (Endpoint) para todas las peticiones
 *   de este controlador. Ayuda a versionar la API (v1).
 * - @RequiredArgsConstructor: Inyecta de manera automática el PacienteService por constructor.
 */
@RestController
@RequestMapping("/api/v1/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    /**
     * Endpoint GET para obtener la lista de todos los pacientes.
     * URL: GET http://localhost:8080/api/v1/pacientes
     */
    @GetMapping
    public ResponseEntity<List<PacienteResponse>> listarTodos() {
        return ResponseEntity.ok(pacienteService.listarTodos());
    }

    /**
     * Endpoint GET para buscar un paciente específico por su ID.
     * URL: GET http://localhost:8080/api/v1/pacientes/{id}
     * 
     * - @PathVariable Long id: Vincula la variable de plantilla de la URL `{id}` al parámetro `id` del método.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    /**
     * Endpoint POST para registrar un nuevo paciente.
     * URL: POST http://localhost:8080/api/v1/pacientes
     * 
     * - @Valid: **Crítico para el examen.** Activa las reglas de validación (como @NotBlank, @Size, @Email) 
     *   que colocamos en la clase PacienteRequest. Si los datos enviados no cumplen con las reglas,
     *   Spring intercepta la petición antes de que llegue a la lógica y arroja un error controlado.
     * - @RequestBody: Le indica a Spring que serialice el JSON del cuerpo del mensaje HTTP a la clase PacienteRequest.
     */
    @PostMapping
    public ResponseEntity<PacienteResponse> registrar(@Valid @RequestBody PacienteRequest request) {
        return new ResponseEntity<>(pacienteService.registrar(request), HttpStatus.CREATED); // Retorna HTTP 201 Created
    }
}
