package com.example.persistence.mapper.utils;

import com.example.persistence.criteria.AbstractCriteria;
import com.example.persistence.dto.AbstractDTO;
import com.example.persistence.entity.AbstractEntity;
import org.mapstruct.Context;

import java.util.List;

public interface EntityMapper<DTO extends AbstractDTO, T extends AbstractEntity, C extends AbstractCriteria<?>> {
	T toEntity(DTO dto);

	List<T> toEntity(Iterable<DTO> dtoList);

	DTO toDTO(T entity, @Context C criteria);

	List<DTO> toDTO(List<T> entities, @Context C criteria);
}
