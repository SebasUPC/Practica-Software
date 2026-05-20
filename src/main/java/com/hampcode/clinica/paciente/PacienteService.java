package com.hampcode.clinica.paciente;

import com.hampcode.clinica.exception.ResourceConflictException;
import com.hampcode.clinica.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Capa de Lógica de Negocio de Pacientes (Servicio).
 * 
 * - @Service: Indica que esta clase es un componente de servicio de Spring (un Bean). 
 *   Contiene la lógica y las validaciones de negocio del sistema.
 * - @RequiredArgsConstructor: Anotación de Lombok que genera automáticamente un constructor 
 *   con todos los atributos marcados como `final`. Esto realiza la **Inyección de Dependencias por Constructor**, 
 *   que es el estándar recomendado en Spring Boot (evita el uso del obsoleto @Autowired).
 */
@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    /**
     * Lista todos los pacientes del sistema.
     * 
     * - @Transactional(readOnly = true): Configura la transacción únicamente para lectura.
     *   Optimiza la sesión de Hibernate ya que no realiza comprobaciones de cambios (dirty checking)
     *   en memoria para la sincronización con base de datos.
     */
    @Transactional(readOnly = true)
    public List<PacienteResponse> listarTodos() {
        return pacienteRepository.findAll().stream()
                .map(pacienteMapper::toResponse)
                .toList();
    }

    /**
     * Busca un paciente específico por su ID.
     * Lanza una excepción ResourceNotFoundException si no lo encuentra.
     */
    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .map(pacienteMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));
    }

    /**
     * Registra un nuevo paciente en la base de datos aplicando reglas de integridad.
     * 
     * - @Transactional: Configura una transacción de escritura activa. Si ocurre algún error dentro de este método,
     *   se ejecuta un ROLLBACK automático de base de datos para mantener la consistencia.
     */
    @Transactional
    public PacienteResponse registrar(PacienteRequest request) {
        // Regla de Negocio: No permitir DNI duplicados
        if (pacienteRepository.existsByDni(request.dni())) {
            throw new ResourceConflictException("El DNI ingresado ya está registrado por otro paciente.");
        }
        
        // Regla de Negocio: No permitir Emails duplicados
        if (pacienteRepository.existsByEmail(request.email())) {
            throw new ResourceConflictException("El email ingresado ya está registrado por otro paciente.");
        }
        
        Paciente paciente = pacienteMapper.toEntity(request);
        return pacienteMapper.toResponse(pacienteRepository.save(paciente));
    }
}
