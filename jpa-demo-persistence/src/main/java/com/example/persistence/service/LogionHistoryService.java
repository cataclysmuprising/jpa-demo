package com.example.persistence.service;

import com.example.persistence.config.PrimaryPersistenceContext;
import com.example.persistence.criteria.LoginHistoryCriteria;
import com.example.persistence.dto.LoginHistoryDTO;
import com.example.persistence.entity.LoginHistory;
import com.example.persistence.mapper.LoginHistoryMapper;
import com.example.persistence.repository.LoginHistoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(transactionManager = PrimaryPersistenceContext.TX_MANAGER, rollbackFor = Exception.class)
public class LogionHistoryService extends BaseService<LoginHistory, LoginHistoryCriteria, LoginHistoryDTO, LoginHistoryMapper> {

	private static final Logger serviceLogger = LogManager.getLogger("serviceLogs." + LogionHistoryService.class);
	private final LoginHistoryRepository repository;
	private final LoginHistoryMapper mapper;

	@Autowired
	LogionHistoryService(LoginHistoryRepository repository, LoginHistoryMapper mapper) {
		super(repository, mapper);
		this.repository = repository;
		this.mapper = mapper;
	}
}
