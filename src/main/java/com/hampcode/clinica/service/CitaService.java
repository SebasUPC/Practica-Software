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

/**
 * Capa de Negocio Central: Servicio de Citas Médicas.
 * 
 * Aquí se concentran las validaciones complejas de integridad horaria (cruce de agendas).
 */
@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    /**
     * Lista todas las citas de la clínica.
     */
    @Transactional(readOnly = true)
    public List<CitaResponse> listarTodas() {
        return citaRepository.findAll().stream()
                .map(ClinicaMapper::toCitaResponse)
                .toList();
    }

    /**
     * Busca una cita específica por su ID.
     */
    @Transactional(readOnly = true)
    public CitaResponse buscarPorId(Long id) {
        return citaRepository.findById(id)
                .map(ClinicaMapper::toCitaResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id: " + id));
    }

    /**
     * REGISTRO DE CITAS (El core del examen):
     * 
     * Implementa un control estricto de concurrencia y validación horaria.
     * - @Transactional: Si alguna de las validaciones falla o la base de datos se cae a mitad del método,
     *   se ejecuta un ROLLBACK de todas las transacciones anteriores, asegurando la consistencia (Propiedad ACID).
     */
    @Transactional
    public CitaResponse registrar(CitaRequest request) {
        // 1. Obtener y validar la existencia física del Paciente en base de datos.
        var paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + request.pacienteId()));

        // 2. Obtener y validar la existencia física del Médico en base de datos.
        var medico = medicoRepository.findById(request.medicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado con id: " + request.medicoId()));

        // 3. Regla de Negocio 1: Un Paciente no puede tener dos citas a la misma hora (citas activas).
        // Si el repositorio retorna true (existe cruce), lanzamos una excepción controlada que responderá HTTP 400.
        if (citaRepository.existeCitaPacienteMismaHora(request.pacienteId(), request.fechaHora())) {
            throw new BusinessRuleException("El paciente " + paciente.getNombre() + " ya cuenta con una cita médica programada para la fecha y hora seleccionada.");
        }

        // 4. Regla de Negocio 2: Un Médico no puede tener dos citas a la misma hora (agenda ocupada).
        if (citaRepository.existeCitaMedicoMismaHora(request.medicoId(), request.fechaHora())) {
            throw new BusinessRuleException("El médico " + medico.getNombre() + " ya tiene programada una cita para la fecha y hora seleccionada.");
        }

        // 5. Mapear y guardar la cita con el estado inicial PENDIENTE.
        Cita cita = Cita.builder()
                .paciente(paciente)
                .medico(medico)
                .fechaHora(request.fechaHora())
                .motivo(request.motivo())
                .estado(EstadoCita.PENDIENTE)
                .build();

        return ClinicaMapper.toCitaResponse(citaRepository.save(cita));
    }

    /**
     * CANCELACIÓN DE CITAS:
     * 
     * Cambia el estado de una cita a CANCELADA, liberando la disponibilidad horaria
     * para que el paciente o el médico puedan volver a agendar en ese mismo horario.
     */
    @Transactional
    public CitaResponse cancelar(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id: " + id));

        // Regla de Negocio: No se puede cancelar una cita que ya estaba cancelada.
        if (cita.getEstado() == EstadoCita.CANCELADA) {
            throw new BusinessRuleException("La cita ya se encuentra cancelada.");
        }

        cita.setEstado(EstadoCita.CANCELADA);
        return ClinicaMapper.toCitaResponse(citaRepository.save(cita));
    }
}
