package com.example.persistence.mapper;

import com.example.persistence.config.MapStructConfig;
import com.example.persistence.criteria.RoleActionCriteria;
import com.example.persistence.dto.RoleActionDTO;
import com.example.persistence.entity.RoleAction;
import com.example.persistence.mapper.decorator.RoleActionDecorator;
import com.example.persistence.mapper.utils.EntityMapper;
import org.mapstruct.Context;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapStructConfig.class)
@DecoratedWith(RoleActionDecorator.class)
public interface RoleActionMapper extends EntityMapper<RoleActionDTO, RoleAction, RoleActionCriteria> {
	@Override
	@Mapping(target = "role", ignore = true)
	@Mapping(target = "action", ignore = true)
	RoleActionDTO toDTO(RoleAction entity, @Context RoleActionCriteria criteria);

	@Override
	@Mapping(target = "role", ignore = true)
	@Mapping(target = "action", ignore = true)
	List<RoleActionDTO> toDTO(List<RoleAction> entities, @Context RoleActionCriteria criteria);
}
