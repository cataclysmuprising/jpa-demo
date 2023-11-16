package com.example.persistence.service;

import com.example.persistence.config.PrimaryPersistenceContext;
import com.example.persistence.criteria.RoleCriteria;
import com.example.persistence.dto.RoleDTO;
import com.example.persistence.entity.Role;
import com.example.persistence.mapper.RoleMapper;
import com.example.persistence.repository.RoleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(transactionManager = PrimaryPersistenceContext.TX_MANAGER, rollbackFor = Exception.class)
public class RoleService extends BaseService<Role, RoleCriteria, RoleDTO, RoleMapper> {

	private static final Logger serviceLogger = LogManager.getLogger("serviceLogs." + RoleService.class);
	private final RoleRepository repository;
	private final RoleMapper mapper;

	@Autowired
	RoleService(RoleRepository repository, RoleMapper mapper) {
		super(repository, mapper);
		this.repository = repository;
		this.mapper = mapper;
	}

	@Transactional(readOnly = true)
	public List<String> selectRolesByActionURL(String actionUrl, String appName) {
		serviceLogger.info("Finding role names by given actionUrl : <{}> and appName : <{}>", actionUrl, appName);
		return repository.selectRolesByActionURL(actionUrl, appName);
	}
}
