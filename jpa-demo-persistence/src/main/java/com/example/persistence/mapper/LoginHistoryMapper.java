package com.example.persistence.mapper;

import com.example.persistence.config.MapStructConfig;
import com.example.persistence.criteria.LoginHistoryCriteria;
import com.example.persistence.dto.LoginHistoryDTO;
import com.example.persistence.entity.LoginHistory;
import com.example.persistence.mapper.utils.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface LoginHistoryMapper extends EntityMapper<LoginHistoryDTO, LoginHistory, LoginHistoryCriteria> {}
