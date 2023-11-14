package com.example.persistence.mapper.decorator;

import com.example.persistence.criteria.AdministratorRoleCriteria;
import com.example.persistence.dto.AdministratorRoleDTO;
import com.example.persistence.entity.AdministratorRole;
import com.example.persistence.mapper.AdministratorRoleMapper;
import com.example.persistence.mapper.utils.MapperFactory;
import org.mapstruct.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public abstract class AdministratorRoleDecorator implements AdministratorRoleMapper {

	@Autowired
	private AdministratorRoleMapper mapper;

	@Override
	public AdministratorRoleDTO toDTO(AdministratorRole entity, @Context AdministratorRoleCriteria criteria) {
		AdministratorRoleDTO dto = mapper.toDTO(entity, criteria);

		if (criteria != null && entity != null) {
			if (criteria.getAdministrator() != null) {
				dto.setAdministrator(MapperFactory.administratorMapper.toDTO(entity.getAdministrator(), criteria.getAdministrator()));
			}
			if (criteria.getRole() != null) {
				dto.setRole(MapperFactory.roleMapper.toDTO(entity.getRole(), criteria.getRole()));
			}
		}

		return dto;
	}

	@Override
	public List<AdministratorRoleDTO> toDTO(List<AdministratorRole> entities, @Context AdministratorRoleCriteria criteria) {
		if (entities == null) {
			return null;
		}
		List<AdministratorRoleDTO> dtos = new ArrayList<>(entities.size());
		for (AdministratorRole entity : entities) {
			dtos.add(toDTO(entity, criteria));
		}

		return dtos;
	}
}
