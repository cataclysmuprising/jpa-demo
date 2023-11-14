package com.example.persistence.service;

import com.example.persistence.config.PrimaryPersistenceContext;
import com.example.persistence.criteria.AdministratorCriteria;
import com.example.persistence.dto.AdministratorDTO;
import com.example.persistence.entity.Administrator;
import com.example.persistence.mapper.AdministratorMapper;
import com.example.persistence.repository.AdministratorRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(transactionManager = PrimaryPersistenceContext.TX_MANAGER, rollbackFor = Exception.class)
public class AdministratorService extends BaseService<Administrator, AdministratorCriteria, AdministratorDTO, AdministratorMapper> {

	private static final Logger serviceLogger = LogManager.getLogger("serviceLogs." + AdministratorService.class);
	private final AdministratorRepository repository;
	private final AdministratorMapper mapper;

	@Autowired
	AdministratorService(AdministratorRepository repository, AdministratorMapper mapper) {
		super(repository, mapper);
		this.repository = repository;
		this.mapper = mapper;
	}
}
