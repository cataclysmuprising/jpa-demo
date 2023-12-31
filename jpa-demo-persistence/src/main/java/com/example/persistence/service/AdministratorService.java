package com.example.persistence.service;

import com.example.persistence.config.PrimaryPersistenceContext;
import com.example.persistence.criteria.AdministratorCriteria;
import com.example.persistence.criteria.AdministratorRoleCriteria;
import com.example.persistence.dto.AdministratorDTO;
import com.example.persistence.dto.AuthenticatedClientDTO;
import com.example.persistence.entity.Administrator;
import com.example.persistence.entity.AdministratorRole;
import com.example.persistence.entity.Role;
import com.example.persistence.mapper.AdministratorMapper;
import com.example.persistence.repository.AdministratorRepository;
import com.example.persistence.repository.AdministratorRoleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(transactionManager = PrimaryPersistenceContext.TX_MANAGER, rollbackFor = Exception.class)
public class AdministratorService extends BaseService<Administrator, AdministratorCriteria, AdministratorDTO, AdministratorMapper> {

	private static final Logger serviceLogger = LogManager.getLogger("serviceLogs." + AdministratorService.class);
	private final AdministratorRepository repository;
	private final AdministratorMapper mapper;

	@Autowired
	private AdministratorRoleRepository administratorRoleRepository;

	@Autowired
	AdministratorService(AdministratorRepository repository, AdministratorMapper mapper) {
		super(repository, mapper);
		this.repository = repository;
		this.mapper = mapper;
	}

	@Transactional(readOnly = true)
	public AdministratorDTO findByLoginId(String loginId, String... hints) {
		serviceLogger.info("Finding single administrator information by using loginId: <{}>", loginId);
		return repository.findByLoginId(loginId, hints).map(value -> mapper.toDTO(value, null)).orElse(null);
	}

	@Transactional(readOnly = true)
	public AuthenticatedClientDTO findAuthenticatedClient(String loginId) {
		AuthenticatedClientDTO authClient = null;
		serviceLogger.info("Finding authenticated administrator information by using loginId: <{}>", loginId);
		Optional<Administrator> administratorInfo = repository.findByLoginId(loginId, "Administrator(roles)");
		if (administratorInfo.isPresent()) {
			Administrator administrator = administratorInfo.get();
			authClient = new AuthenticatedClientDTO();
			authClient.setLoginId(administrator.getLoginId());
			authClient.setClientType(AuthenticatedClientDTO.ClientType.ADMINISTRATOR);
			authClient.setContentId(administrator.getContentId());
			authClient.setId(administrator.getId());
			authClient.setName(administrator.getName());
			authClient.setPassword(administrator.getPassword());
			authClient.setStatus(administrator.getStatus());
			authClient.setRecordRegDate(administrator.getRecordRegDate());
			authClient.setRecordUpdDate(administrator.getRecordUpdDate());
			List<Role> roles = administrator.getRoles();
			authClient.setRoles(roles.stream().map(Role::getName).toList());
			authClient.setRoleIds(roles.stream().map(Role::getId).toList());
		}

		return authClient;
	}

	public long createNewAdminWithRoles(AdministratorDTO adminDTO, Set<Long> roleIds, long recordRegId) {
		long lastInsertedId;
		Administrator newAdministrator = mapper.toEntity(adminDTO);
		newAdministrator.setRecordRegId(recordRegId);
		newAdministrator.setRecordUpdId(recordRegId);
		newAdministrator = repository.save(newAdministrator);

		if (!CollectionUtils.isEmpty(roleIds)) {
			List<AdministratorRole> adminRoles = new ArrayList<>();
			for (long roleId : roleIds) {
				AdministratorRole adminRole = new AdministratorRole(newAdministrator.getId(), roleId);
				adminRole.setRecordRegId(recordRegId);
				adminRole.setRecordUpdId(recordRegId);
				adminRoles.add(adminRole);
			}
			administratorRoleRepository.saveAll(adminRoles);
		}
		else {
			// register with 'Administrator' Role
			AdministratorRole adminRole = new AdministratorRole(newAdministrator.getId(), 2L);
			adminRole.setRecordRegId(recordRegId);
			adminRole.setRecordUpdId(recordRegId);
			administratorRoleRepository.save(adminRole);
		}
		return newAdministrator.getId();
	}

	public void updateAdminWithRoles(AdministratorDTO adminDTO, Set<Long> roleIds, long recordUpdId) {
		Administrator updateAdministartor = new Administrator();
		updateAdministartor.setName(adminDTO.getName());
		updateAdministartor.setStatus(adminDTO.getStatus());
		updateAdministartor.setRecordUpdId(recordUpdId);

		// update administrator
		AdministratorCriteria criteria = new AdministratorCriteria();
		criteria.setId(adminDTO.getId());
		repository.update(updateAdministartor, criteria);

		if (!CollectionUtils.isEmpty(roleIds)) {
			// remove existing roles
			AdministratorRoleCriteria administratorRoleCriteria = new AdministratorRoleCriteria();
			administratorRoleCriteria.setAdministratorId(adminDTO.getId());
			administratorRoleRepository.deleteByCriteria(administratorRoleCriteria);

			// add new roles
			List<AdministratorRole> adminRoles = new ArrayList<>();
			for (long roleId : roleIds) {
				AdministratorRole adminRole = new AdministratorRole(adminDTO.getId(), roleId);
				adminRole.setRecordRegId(recordUpdId);
				adminRole.setRecordUpdId(recordUpdId);
				adminRoles.add(adminRole);
			}
			administratorRoleRepository.saveAll(adminRoles);
		}
	}
}
