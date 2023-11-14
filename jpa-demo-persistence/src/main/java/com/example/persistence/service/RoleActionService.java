package com.example.persistence.service;

import com.example.persistence.config.PrimaryPersistenceContext;
import com.example.persistence.criteria.RoleActionCriteria;
import com.example.persistence.dto.RoleActionDTO;
import com.example.persistence.entity.RoleAction;
import com.example.persistence.mapper.RoleActionMapper;
import com.example.persistence.repository.RoleActionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(transactionManager = PrimaryPersistenceContext.TX_MANAGER, rollbackFor = Exception.class)
public class RoleActionService extends BaseService<RoleAction, RoleActionCriteria, RoleActionDTO, RoleActionMapper> {

	private static final Logger serviceLogger = LogManager.getLogger("serviceLogs." + RoleActionService.class);
	private final RoleActionRepository repository;
	private final RoleActionMapper mapper;

	@Autowired
	RoleActionService(RoleActionRepository repository, RoleActionMapper mapper) {
		super(repository, mapper);
		this.repository = repository;
		this.mapper = mapper;
	}
}
