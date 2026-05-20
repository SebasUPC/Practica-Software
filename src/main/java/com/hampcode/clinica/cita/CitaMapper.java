package com.hampcode.clinica.cita;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CitaMapper {

    @Mapping(source = "paciente.id", target = "pacienteId")
    @Mapping(source = "paciente.nombre", target = "pacienteNombre")
    @Mapping(source = "medico.id", target = "medicoId")
    @Mapping(source = "medico.nombre", target = "medicoNombre")
    @Mapping(source = "medico.especialidad", target = "medicoEspecialidad")
    CitaResponse toResponse(Cita entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "paciente", ignore = true)
    @Mapping(target = "medico", ignore = true)
    Cita toEntity(CitaRequest request);
}
