package com.example.persistence.mapper.decorator;

import com.example.persistence.criteria.RoleActionCriteria;
import com.example.persistence.dto.RoleActionDTO;
import com.example.persistence.entity.RoleAction;
import com.example.persistence.mapper.RoleActionMapper;
import com.example.persistence.mapper.utils.MapperFactory;
import org.mapstruct.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public abstract class RoleActionDecorator implements RoleActionMapper {

	@Autowired
	private RoleActionMapper mapper;

	@Override
	public RoleActionDTO toDTO(RoleAction entity, @Context RoleActionCriteria criteria) {
		RoleActionDTO dto = mapper.toDTO(entity, criteria);

		if (criteria != null && entity != null) {
			if (criteria.getRole() != null) {
				dto.setRole(MapperFactory.roleMapper.toDTO(entity.getRole(), criteria.getRole()));
			}
			if (criteria.getAction() != null) {
				dto.setAction(MapperFactory.actionMapper.toDTO(entity.getAction(), criteria.getAction()));
			}
		}

		return dto;
	}

	@Override
	public List<RoleActionDTO> toDTO(List<RoleAction> entities, @Context RoleActionCriteria criteria) {
		if (entities == null) {
			return null;
		}
		List<RoleActionDTO> dtos = new ArrayList<>(entities.size());
		for (RoleAction entity : entities) {
			dtos.add(toDTO(entity, criteria));
		}

		return dtos;
	}
}
