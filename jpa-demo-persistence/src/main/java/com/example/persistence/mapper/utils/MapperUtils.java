package com.example.persistence.mapper.utils;

import com.example.persistence.dto.AbstractDTO;
import com.example.persistence.entity.AbstractEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public class MapperUtils<DTO extends AbstractDTO> {
	public static <DTO extends AbstractDTO> PaginatedResult<DTO> toPaginatedResult(Page<? extends AbstractEntity> page, List<DTO> data) {
		return new PaginatedResult<>(page.getTotalElements(), page.getTotalPages(), page.getSize(), page.getNumber(), page.getNumberOfElements(), page.getSort(), data);
	}
}
