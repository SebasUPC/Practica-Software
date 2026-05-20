package com.hampcode.clinica.medico;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicoMapper {

    MedicoResponse toResponse(Medico entity);

    @Mapping(target = "id", ignore = true)
    Medico toEntity(MedicoRequest request);
}
