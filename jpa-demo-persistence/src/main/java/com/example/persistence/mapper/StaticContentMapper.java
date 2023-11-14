package com.example.persistence.mapper;

import com.example.persistence.config.MapStructConfig;
import com.example.persistence.criteria.StaticContentCriteria;
import com.example.persistence.dto.StaticContentDTO;
import com.example.persistence.entity.StaticContent;
import com.example.persistence.mapper.utils.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface StaticContentMapper extends EntityMapper<StaticContentDTO, StaticContent, StaticContentCriteria> {}
