package com.hampcode.clinica.service;

import com.hampcode.clinica.domain.Medico;
import com.hampcode.clinica.dto.MedicoRequest;
import com.hampcode.clinica.dto.MedicoResponse;
import com.hampcode.clinica.exception.BusinessRuleException;
import com.hampcode.clinica.exception.ResourceNotFoundException;
import com.hampcode.clinica.mapper.ClinicaMapper;
import com.hampcode.clinica.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;

    @Transactional(readOnly = true)
    public List<MedicoResponse> listarTodos() {
        return medicoRepository.findAll().stream()
                .map(ClinicaMapper::toMedicoResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MedicoResponse buscarPorId(Long id) {
        return medicoRepository.findById(id)
                .map(ClinicaMapper::toMedicoResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado con id: " + id));
    }

    @Transactional
    public MedicoResponse registrar(MedicoRequest request) {
        if (medicoRepository.existsByEmail(request.email())) {
            throw new BusinessRuleException("El email ingresado ya está registrado por otro médico.");
        }
        Medico medico = ClinicaMapper.toMedicoEntity(request);
        return ClinicaMapper.toMedicoResponse(medicoRepository.save(medico));
    }
}
