package com.hampcode.clinica.service;

import com.hampcode.clinica.domain.Cita;
import com.hampcode.clinica.domain.EstadoCita;
import com.hampcode.clinica.dto.CitaRequest;
import com.hampcode.clinica.dto.CitaResponse;
import com.hampcode.clinica.exception.BusinessRuleException;
import com.hampcode.clinica.exception.ResourceNotFoundException;
import com.hampcode.clinica.mapper.ClinicaMapper;
import com.hampcode.clinica.repository.CitaRepository;
import com.hampcode.clinica.repository.MedicoRepository;
import com.hampcode.clinica.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    @Transactional(readOnly = true)
    public List<CitaResponse> listarTodas() {
        return citaRepository.findAll().stream()
                .map(ClinicaMapper::toCitaResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CitaResponse buscarPorId(Long id) {
        return citaRepository.findById(id)
                .map(ClinicaMapper::toCitaResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id: " + id));
    }

    @Transactional
    public CitaResponse registrar(CitaRequest request) {
        // 1. Obtener y validar Paciente
        var paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + request.pacienteId()));

        // 2. Obtener y validar Médico
        var medico = medicoRepository.findById(request.medicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado con id: " + request.medicoId()));

        // 3. Regla de Negocio 1: Paciente no puede tener citas duplicadas a la misma hora
        if (citaRepository.existeCitaPacienteMismaHora(request.pacienteId(), request.fechaHora())) {
            throw new BusinessRuleException("El paciente " + paciente.getNombre() + " ya cuenta con una cita médica programada para la fecha y hora seleccionada.");
        }

        // 4. Regla de Negocio 2: Médico no puede tener citas duplicadas a la misma hora
        if (citaRepository.existeCitaMedicoMismaHora(request.medicoId(), request.fechaHora())) {
            throw new BusinessRuleException("El médico " + medico.getNombre() + " ya tiene programada una cita para la fecha y hora seleccionada.");
        }

        // 5. Construir y guardar la cita
        Cita cita = Cita.builder()
                .paciente(paciente)
                .medico(medico)
                .fechaHora(request.fechaHora())
                .motivo(request.motivo())
                .estado(EstadoCita.PENDIENTE)
                .build();

        return ClinicaMapper.toCitaResponse(citaRepository.save(cita));
    }

    @Transactional
    public CitaResponse cancelar(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id: " + id));

        if (cita.getEstado() == EstadoCita.CANCELADA) {
            throw new BusinessRuleException("La cita ya se encuentra cancelada.");
        }

        cita.setEstado(EstadoCita.CANCELADA);
        return ClinicaMapper.toCitaResponse(citaRepository.save(cita));
    }
}
