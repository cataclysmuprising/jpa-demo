package com.example.persistence.mapper.decorator;

import com.example.persistence.criteria.RoleCriteria;
import com.example.persistence.dto.RoleDTO;
import com.example.persistence.entity.Role;
import com.example.persistence.mapper.RoleMapper;
import com.example.persistence.mapper.utils.MapperFactory;
import org.mapstruct.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public abstract class RoleDecorator implements RoleMapper {

	@Autowired
	private RoleMapper mapper;

	@Override
	public RoleDTO toDTO(Role entity, @Context RoleCriteria criteria) {
		RoleDTO dto = mapper.toDTO(entity, criteria);
		if (criteria != null && entity != null) {
			if (criteria.getAdministrator() != null) {
				dto.setAdministrators(MapperFactory.administratorMapper.toDTO(entity.getAdministrators(), criteria.getAdministrator()));
			}
			if (criteria.getAction() != null) {
				dto.setActions(MapperFactory.actionMapper.toDTO(entity.getActions(), criteria.getAction()));
			}
		}
		return dto;
	}

	@Override
	public List<RoleDTO> toDTO(List<Role> entities, @Context RoleCriteria criteria) {
		if (entities == null) {
			return null;
		}
		List<RoleDTO> dtos = new ArrayList<>(entities.size());
		for (Role entity : entities) {
			dtos.add(toDTO(entity, criteria));
		}

		return dtos;
	}
}
