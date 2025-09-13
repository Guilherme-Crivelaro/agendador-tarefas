package com.guilherme.agendadortarefas.business.mapper;

import com.guilherme.agendadortarefas.business.dto.TarefasDTORecord;
import com.guilherme.agendadortarefas.infrastructure.entity.TarefasEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public interface TarefaUpdateConverter {

    void updateDeTarefas(TarefasDTORecord dto, @MappingTarget TarefasEntity entity);
}

