package com.example.persistence.mapper;

import com.example.persistence.config.MapStructConfig;
import com.example.persistence.criteria.AdministratorRoleCriteria;
import com.example.persistence.dto.AdministratorRoleDTO;
import com.example.persistence.entity.AdministratorRole;
import com.example.persistence.mapper.decorator.AdministratorRoleDecorator;
import com.example.persistence.mapper.utils.EntityMapper;
import org.mapstruct.Context;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapStructConfig.class)
@DecoratedWith(AdministratorRoleDecorator.class)
public interface AdministratorRoleMapper extends EntityMapper<AdministratorRoleDTO, AdministratorRole, AdministratorRoleCriteria> {
	@Override
	@Mapping(target = "administrator", ignore = true)
	@Mapping(target = "role", ignore = true)
	AdministratorRoleDTO toDTO(AdministratorRole entity, @Context AdministratorRoleCriteria criteria);

	@Override
	@Mapping(target = "administrator", ignore = true)
	@Mapping(target = "role", ignore = true)
	List<AdministratorRoleDTO> toDTO(List<AdministratorRole> entities, @Context AdministratorRoleCriteria criteria);
}
