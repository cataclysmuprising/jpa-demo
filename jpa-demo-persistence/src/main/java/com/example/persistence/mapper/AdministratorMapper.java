package com.example.persistence.mapper;

import com.example.persistence.config.MapStructConfig;
import com.example.persistence.criteria.AdministratorCriteria;
import com.example.persistence.dto.AdministratorDTO;
import com.example.persistence.entity.Administrator;
import com.example.persistence.mapper.decorator.AdministratorDecorator;
import com.example.persistence.mapper.utils.EntityMapper;
import org.mapstruct.Context;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapStructConfig.class)
@DecoratedWith(AdministratorDecorator.class)
public interface AdministratorMapper extends EntityMapper<AdministratorDTO, Administrator, AdministratorCriteria> {
	@Override
	@Mapping(target = "staticContent", ignore = true)
	@Mapping(target = "roles", ignore = true)
	AdministratorDTO toDTO(Administrator entity, @Context AdministratorCriteria criteria);

	@Override
	@Mapping(target = "staticContent", ignore = true)
	@Mapping(target = "roles", ignore = true)
	List<AdministratorDTO> toDTO(List<Administrator> entities, @Context AdministratorCriteria criteria);
}
