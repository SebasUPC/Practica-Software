package com.hampcode.clinica.service;

import com.hampcode.clinica.domain.Paciente;
import com.hampcode.clinica.dto.PacienteRequest;
import com.hampcode.clinica.dto.PacienteResponse;
import com.hampcode.clinica.exception.BusinessRuleException;
import com.hampcode.clinica.exception.ResourceNotFoundException;
import com.hampcode.clinica.mapper.ClinicaMapper;
import com.hampcode.clinica.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    @Transactional(readOnly = true)
    public List<PacienteResponse> listarTodos() {
        return pacienteRepository.findAll().stream()
                .map(ClinicaMapper::toPacienteResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .map(ClinicaMapper::toPacienteResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));
    }

    @Transactional
    public PacienteResponse registrar(PacienteRequest request) {
        if (pacienteRepository.existsByDni(request.dni())) {
            throw new BusinessRuleException("El DNI ingresado ya está registrado por otro paciente.");
        }
        if (pacienteRepository.existsByEmail(request.email())) {
            throw new BusinessRuleException("El email ingresado ya está registrado por otro paciente.");
        }
        Paciente paciente = ClinicaMapper.toPacienteEntity(request);
        return ClinicaMapper.toPacienteResponse(pacienteRepository.save(paciente));
    }
}
