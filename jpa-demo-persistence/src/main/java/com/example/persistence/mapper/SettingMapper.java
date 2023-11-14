package com.example.persistence.mapper;

import com.example.persistence.config.MapStructConfig;
import com.example.persistence.criteria.SettingCriteria;
import com.example.persistence.dto.SettingDTO;
import com.example.persistence.entity.Setting;
import com.example.persistence.mapper.utils.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface SettingMapper extends EntityMapper<SettingDTO, Setting, SettingCriteria> {}
