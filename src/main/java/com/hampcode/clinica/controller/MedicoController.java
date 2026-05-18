package com.hampcode.clinica.controller;

import com.hampcode.clinica.dto.MedicoRequest;
import com.hampcode.clinica.dto.MedicoResponse;
import com.hampcode.clinica.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    @GetMapping
    public ResponseEntity<List<MedicoResponse>> listarTodos() {
        return ResponseEntity.ok(medicoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<MedicoResponse> registrar(@Valid @RequestBody MedicoRequest request) {
        return new ResponseEntity<>(medicoService.registrar(request), HttpStatus.CREATED);
    }
}
