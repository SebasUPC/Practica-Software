package com.hampcode.clinica.medico;

import com.hampcode.clinica.exception.ResourceConflictException;
import com.hampcode.clinica.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Capa de Lógica de Negocio de Médicos.
 */
@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;

    /**
     * Lista todos los médicos registrados.
     */
    @Transactional(readOnly = true)
    public List<MedicoResponse> listarTodos() {
        return medicoRepository.findAll().stream()
                .map(medicoMapper::toResponse)
                .toList();
    }

    /**
     * Busca un médico por su ID o lanza un error descriptivo de no encontrado.
     */
    @Transactional(readOnly = true)
    public MedicoResponse buscarPorId(Long id) {
        return medicoRepository.findById(id)
                .map(medicoMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado con id: " + id));
    }

    /**
     * Registra un nuevo médico asegurando la unicidad del email corporativo.
     */
    @Transactional
    public MedicoResponse registrar(MedicoRequest request) {
        if (medicoRepository.existsByEmail(request.email())) {
            throw new ResourceConflictException("El email ingresado ya está registrado por otro médico.");
        }
        
        Medico medico = medicoMapper.toEntity(request);
        return medicoMapper.toResponse(medicoRepository.save(medico));
    }
}
