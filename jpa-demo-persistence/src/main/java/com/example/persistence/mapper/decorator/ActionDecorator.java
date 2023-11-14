package com.example.persistence.mapper.decorator;

import com.example.persistence.criteria.ActionCriteria;
import com.example.persistence.dto.ActionDTO;
import com.example.persistence.entity.Action;
import com.example.persistence.mapper.ActionMapper;
import com.example.persistence.mapper.utils.MapperFactory;
import org.mapstruct.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public abstract class ActionDecorator implements ActionMapper {

	@Autowired
	private ActionMapper mapper;

	@Override
	public ActionDTO toDTO(Action entity, @Context ActionCriteria criteria) {
		ActionDTO dto = mapper.toDTO(entity, criteria);

		if (criteria != null && entity != null) {
			if (criteria.getRole() != null) {
				dto.setRoles(MapperFactory.roleMapper.toDTO(entity.getRoles(), criteria.getRole()));
			}
		}
		return dto;
	}

	@Override
	public List<ActionDTO> toDTO(List<Action> entities, @Context ActionCriteria criteria) {
		if (entities == null) {
			return null;
		}
		List<ActionDTO> dtos = new ArrayList<>(entities.size());
		for (Action entity : entities) {
			dtos.add(toDTO(entity, criteria));
		}

		return dtos;
	}
}
