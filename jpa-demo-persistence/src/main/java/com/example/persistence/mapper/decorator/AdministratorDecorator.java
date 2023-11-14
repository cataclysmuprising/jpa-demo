package com.example.persistence.mapper.decorator;

import com.example.persistence.criteria.AdministratorCriteria;
import com.example.persistence.dto.AdministratorDTO;
import com.example.persistence.entity.Administrator;
import com.example.persistence.mapper.AdministratorMapper;
import com.example.persistence.mapper.utils.MapperFactory;
import org.mapstruct.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public abstract class AdministratorDecorator implements AdministratorMapper {

	@Autowired
	private AdministratorMapper mapper;

	@Override
	public AdministratorDTO toDTO(Administrator entity, @Context AdministratorCriteria criteria) {
		AdministratorDTO dto = mapper.toDTO(entity, criteria);
		if (criteria != null && entity != null) {
			if (criteria.getStaticContent() != null) {
				dto.setStaticContent(MapperFactory.staticContentMapper.toDTO(entity.getStaticContent(), criteria.getStaticContent()));
			}
			if (criteria.getRole() != null) {
				dto.setRoles(MapperFactory.roleMapper.toDTO(entity.getRoles(), criteria.getRole()));
			}
		}
		return dto;
	}

	@Override
	public List<AdministratorDTO> toDTO(List<Administrator> entities, @Context AdministratorCriteria criteria) {
		if (entities == null) {
			return null;
		}
		List<AdministratorDTO> dtos = new ArrayList<>(entities.size());
		for (Administrator entity : entities) {
			dtos.add(toDTO(entity, criteria));
		}

		return dtos;
	}
}
