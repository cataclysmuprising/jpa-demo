package com.example.persistence.mapper;

import com.example.persistence.config.MapStructConfig;
import com.example.persistence.criteria.ActionCriteria;
import com.example.persistence.dto.ActionDTO;
import com.example.persistence.entity.Action;
import com.example.persistence.mapper.decorator.ActionDecorator;
import com.example.persistence.mapper.utils.EntityMapper;
import org.mapstruct.Context;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapStructConfig.class)
@DecoratedWith(ActionDecorator.class)
public interface ActionMapper extends EntityMapper<ActionDTO, Action, ActionCriteria> {
	@Override
	@Mapping(target = "roles", ignore = true)
	ActionDTO toDTO(Action entity, @Context ActionCriteria criteria);

	@Override
	@Mapping(target = "roles", ignore = true)
	List<ActionDTO> toDTO(List<Action> entities, @Context ActionCriteria criteria);
}
