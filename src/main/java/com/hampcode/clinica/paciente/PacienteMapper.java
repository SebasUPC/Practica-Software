package com.hampcode.clinica.paciente;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PacienteMapper {

    PacienteResponse toResponse(Paciente entity);

    @Mapping(target = "id", ignore = true)
    Paciente toEntity(PacienteRequest request);
}
