package com.hampcode.clinica.mapper;

import com.hampcode.clinica.domain.Cita;
import com.hampcode.clinica.domain.Medico;
import com.hampcode.clinica.domain.Paciente;
import com.hampcode.clinica.dto.*;

public class ClinicaMapper {

    public static PacienteResponse toPacienteResponse(Paciente entity) {
        if (entity == null) return null;
        return new PacienteResponse(
            entity.getId(),
            entity.getNombre(),
            entity.getDni(),
            entity.getEmail()
        );
    }

    public static Paciente toPacienteEntity(PacienteRequest request) {
        if (request == null) return null;
        return Paciente.builder()
            .nombre(request.nombre())
            .dni(request.dni())
            .email(request.email())
            .build();
    }

    public static MedicoResponse toMedicoResponse(Medico entity) {
        if (entity == null) return null;
        return new MedicoResponse(
            entity.getId(),
            entity.getNombre(),
            entity.getEspecialidad(),
            entity.getEmail()
        );
    }

    public static Medico toMedicoEntity(MedicoRequest request) {
        if (request == null) return null;
        return Medico.builder()
            .nombre(request.nombre())
            .especialidad(request.especialidad())
            .email(request.email())
            .build();
    }

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
