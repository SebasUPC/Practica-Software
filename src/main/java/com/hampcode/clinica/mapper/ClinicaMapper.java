package com.hampcode.clinica.mapper;

import com.hampcode.clinica.domain.Cita;
import com.hampcode.clinica.domain.Medico;
import com.hampcode.clinica.domain.Paciente;
import com.hampcode.clinica.dto.*;

/**
 * Clase Utilitaria de Mapeo Manual (ClinicaMapper).
 * 
 * En el desarrollo de software corporativo se suelen usar librerías automáticas de mapeo como MapStruct o ModelMapper.
 * Sin embargo, en un ambiente de examen presencial o de tiempo limitado:
 * 1. Configurar MapStruct puede fallar por problemas en el procesador de anotaciones del compilador.
 * 2. ModelMapper puede degradar el rendimiento por su uso intensivo de "Reflexión Java".
 * Un mapeador manual estático es la solución más rápida, confiable, 100% libre de dependencias
 * y fácil de depurar durante tu examen.
 */
public class ClinicaMapper {

    /**
     * Mapea una entidad Paciente a un PacienteResponse DTO plano.
     */
    public static PacienteResponse toPacienteResponse(Paciente entity) {
        if (entity == null) return null;
        return new PacienteResponse(
            entity.getId(),
            entity.getNombre(),
            entity.getDni(),
            entity.getEmail()
        );
    }

    /**
     * Mapea un PacienteRequest DTO a una nueva entidad Paciente lista para ser persistida.
     * El ID no se asigna porque la base de datos lo autogenera.
     */
    public static Paciente toPacienteEntity(PacienteRequest request) {
        if (request == null) return null;
        return Paciente.builder()
            .nombre(request.nombre())
            .dni(request.dni())
            .email(request.email())
            .build();
    }

    /**
     * Mapea una entidad Medico a un MedicoResponse DTO plano.
     */
    public static MedicoResponse toMedicoResponse(Medico entity) {
        if (entity == null) return null;
        return new MedicoResponse(
            entity.getId(),
            entity.getNombre(),
            entity.getEspecialidad(),
            entity.getEmail()
        );
    }

    /**
     * Mapea un MedicoRequest DTO a una nueva entidad Medico lista para persistir.
     */
    public static Medico toMedicoEntity(MedicoRequest request) {
        if (request == null) return null;
        return Medico.builder()
            .nombre(request.nombre())
            .especialidad(request.especialidad())
            .email(request.email())
            .build();
    }

    /**
     * Mapea una entidad Cita a una respuesta CitaResponse aplanada y amigable.
     * 
     * ¿Por qué este mapeo es crucial?
     * Evita la serialización recursiva e infinita. Extrae de forma segura el nombre del paciente
     * y médico navegando por las relaciones cargadas perezosamente (Lazy), extrayendo solo el texto plano necesario.
     */
    public static CitaResponse toCitaResponse(Cita entity) {
        if (entity == null) return null;
        return new CitaResponse(
            entity.getId(),
            entity.getFechaHora(),
            entity.getPaciente() != null ? entity.getPaciente().getId() : null,
            entity.getPaciente() != null ? entity.getPaciente().getNombre() : null,
            entity.getMedico() != null ? entity.getMedico().getId() : null,
            entity.getMedico() != null ? entity.getMedico().getNombre() : null,
            entity.getMedico() != null ? entity.getMedico().getEspecialidad() : null,
            entity.getMotivo(),
            entity.getEstado()
        );
    }
}
