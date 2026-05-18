package com.hampcode.clinica.controller;

import com.hampcode.clinica.dto.CitaRequest;
import com.hampcode.clinica.dto.CitaResponse;
import com.hampcode.clinica.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST encargado de exponer las operaciones asociadas a Citas Médicas.
 */
@RestController
@RequestMapping("/api/v1/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    /**
     * Endpoint GET para listar todas las citas de la clínica.
     * URL: GET http://localhost:8080/api/v1/citas
     */
    @GetMapping
    public ResponseEntity<List<CitaResponse>> listarTodas() {
        return ResponseEntity.ok(citaService.listarTodas());
    }

    /**
     * Endpoint GET para buscar una cita médica por su ID.
     * URL: GET http://localhost:8080/api/v1/citas/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CitaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.buscarPorId(id));
    }

    /**
     * Endpoint POST para registrar (programar) una nueva cita médica.
     * URL: POST http://localhost:8080/api/v1/citas
     * 
     * - @Valid: Valida automáticamente los campos del CitaRequest (ej. que la fechaHora sea en el futuro).
     */
    @PostMapping
    public ResponseEntity<CitaResponse> registrar(@Valid @RequestBody CitaRequest request) {
        return new ResponseEntity<>(citaService.registrar(request), HttpStatus.CREATED); // Retorna HTTP 201 Created
    }

    /**
     * Endpoint PUT para cancelar una cita médica existente.
     * URL: PUT http://localhost:8080/api/v1/citas/{id}/cancelar
     * 
     * ¿Por qué usamos PUT en lugar de DELETE?
     * En el desarrollo de software moderno y arquitecturas REST robustas, **los registros nunca se eliminan físicamente (hard delete)** 
     * con DELETE a menos que sea estrictamente necesario. En su lugar, hacemos un **borrado lógico (soft delete)**, 
     * actualizando su estado a CANCELADA a través de una petición PUT. Esto mantiene la auditabilidad histórica del sistema.
     */
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<CitaResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.cancelar(id));
    }
}
