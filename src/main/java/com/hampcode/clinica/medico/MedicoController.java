package com.hampcode.clinica.medico;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para gestionar Médicos.
 */
@RestController
@RequestMapping("/api/v1/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    /**
     * Endpoint GET para obtener la lista de todos los médicos.
     * URL: GET http://localhost:8080/api/v1/medicos
     */
    @GetMapping
    public ResponseEntity<List<MedicoResponse>> listarTodos() {
        return ResponseEntity.ok(medicoService.listarTodos());
    }

    /**
     * Endpoint GET para buscar un médico por su ID.
     * URL: GET http://localhost:8080/api/v1/medicos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.buscarPorId(id));
    }

    /**
     * Endpoint POST para registrar un nuevo médico.
     * URL: POST http://localhost:8080/api/v1/medicos
     */
    @PostMapping
    public ResponseEntity<MedicoResponse> registrar(@Valid @RequestBody MedicoRequest request) {
        return new ResponseEntity<>(medicoService.registrar(request), HttpStatus.CREATED); // Retorna HTTP 201 Created
    }
}
