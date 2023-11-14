package com.example.persistence.service;

import com.example.persistence.config.PrimaryPersistenceContext;
import com.example.persistence.criteria.ActionCriteria;
import com.example.persistence.dto.ActionDTO;
import com.example.persistence.entity.Action;
import com.example.persistence.mapper.ActionMapper;
import com.example.persistence.repository.ActionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(transactionManager = PrimaryPersistenceContext.TX_MANAGER, rollbackFor = Exception.class)
public class ActionService extends BaseService<Action, ActionCriteria, ActionDTO, ActionMapper> {

	private static final Logger serviceLogger = LogManager.getLogger("serviceLogs." + ActionService.class);
	private final ActionRepository repository;
	private final ActionMapper mapper;

	@Autowired
	ActionService(ActionRepository repository, ActionMapper mapper) {
		super(repository, mapper);
		this.repository = repository;
		this.mapper = mapper;
	}
}
