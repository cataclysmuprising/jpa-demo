package com.example.persistence.mapper;

import com.example.persistence.config.MapStructConfig;
import com.example.persistence.criteria.RoleCriteria;
import com.example.persistence.dto.RoleDTO;
import com.example.persistence.entity.Role;
import com.example.persistence.mapper.decorator.RoleDecorator;
import com.example.persistence.mapper.utils.EntityMapper;
import org.mapstruct.Context;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapStructConfig.class)
@DecoratedWith(RoleDecorator.class)
public interface RoleMapper extends EntityMapper<RoleDTO, Role, RoleCriteria> {
	@Mapping(target = "administrators", ignore = true)
	@Mapping(target = "actions", ignore = true)
	@Override
	RoleDTO toDTO(Role entity, @Context RoleCriteria criteria);

	@Override
	@Mapping(target = "administrators", ignore = true)
	@Mapping(target = "actions", ignore = true)
	List<RoleDTO> toDTO(List<Role> entities, @Context RoleCriteria criteria);
}
