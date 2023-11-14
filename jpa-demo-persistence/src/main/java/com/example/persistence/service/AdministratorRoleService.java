package com.example.persistence.service;

import com.example.persistence.config.PrimaryPersistenceContext;
import com.example.persistence.criteria.AdministratorRoleCriteria;
import com.example.persistence.dto.AdministratorRoleDTO;
import com.example.persistence.entity.AdministratorRole;
import com.example.persistence.mapper.AdministratorRoleMapper;
import com.example.persistence.repository.AdministratorRoleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(transactionManager = PrimaryPersistenceContext.TX_MANAGER, rollbackFor = Exception.class)
public class AdministratorRoleService extends BaseService<AdministratorRole, AdministratorRoleCriteria, AdministratorRoleDTO, AdministratorRoleMapper> {

	private static final Logger serviceLogger = LogManager.getLogger("serviceLogs." + AdministratorRoleService.class);
	private final AdministratorRoleRepository repository;
	private final AdministratorRoleMapper mapper;

	@Autowired
	AdministratorRoleService(AdministratorRoleRepository repository, AdministratorRoleMapper mapper) {
		super(repository, mapper);
		this.repository = repository;
		this.mapper = mapper;
	}
}
