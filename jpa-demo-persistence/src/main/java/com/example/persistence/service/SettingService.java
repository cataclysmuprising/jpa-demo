package com.example.persistence.service;

import com.example.persistence.config.PrimaryPersistenceContext;
import com.example.persistence.criteria.SettingCriteria;
import com.example.persistence.dto.SettingDTO;
import com.example.persistence.entity.Setting;
import com.example.persistence.mapper.SettingMapper;
import com.example.persistence.repository.SettingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(transactionManager = PrimaryPersistenceContext.TX_MANAGER, rollbackFor = Exception.class)
public class SettingService extends BaseService<Setting, SettingCriteria, SettingDTO, SettingMapper> {

	private static final Logger serviceLogger = LogManager.getLogger("serviceLogs." + SettingService.class);
	private final SettingRepository repository;
	private final SettingMapper mapper;

	@Autowired
	SettingService(SettingRepository repository, SettingMapper mapper) {
		super(repository, mapper);
		this.repository = repository;
		this.mapper = mapper;
	}
}
