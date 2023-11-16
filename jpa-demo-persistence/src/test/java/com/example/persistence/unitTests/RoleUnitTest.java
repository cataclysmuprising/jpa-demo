package com.example.persistence.unitTests;

import com.example.persistence.CommonTestBase;
import com.example.persistence.criteria.RoleCriteria;
import com.example.persistence.entity.Role;
import com.example.persistence.repository.RoleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.testng.annotations.Test;

public class RoleUnitTest extends CommonTestBase {
	private static final Logger testLogger = LogManager.getLogger("testLogs." + RoleUnitTest.class.getName());

	@Autowired
	private RoleRepository repository;

	@Test
	public void findAll() {
		repository.findAll(new RoleCriteria().getFilter()).iterator().forEachRemaining(testLogger::info);
	}

//	@Test
//	public void findOneById() {
//		Long findId = 1L;
//		repository.findById(findId).ifPresent(testLogger::info);
//	}

	@Test
	public void findOneByCriteria() {
		RoleCriteria criteria = new RoleCriteria();
		criteria.setId(1L);
		criteria.setAppName("myapp-backend");
		criteria.setName("Super-User");
		criteria.setRoleType(Role.RoleType.SYSTEM);
		repository.findOne(criteria.getFilter()).ifPresent(testLogger::info);
	}

	@Test
	public void findAllByCriteria() {
		RoleCriteria criteria = new RoleCriteria();
		criteria.setKeyword("user");
		repository.findAll(criteria.getFilter()).iterator().forEachRemaining(testLogger::info);
	}

	@Test
	public void selectRolesByActionURL() {
		RoleCriteria criteria = new RoleCriteria();
		criteria.setKeyword("user");
		repository.selectRolesByActionURL("/user/1", "myapp-backend").iterator().forEachRemaining(testLogger::info);
	}

	@Test
	public void findAllByCriteriaWithSort() {
		RoleCriteria criteria = new RoleCriteria();
		criteria.setId(1L);
		criteria.setAppName("myapp-backend");
		criteria.setName("Super-User");
		criteria.setRoleType(Role.RoleType.SYSTEM);
		// multiple sorting
		// Sort sort = criteria.getSort("country", "asc").and(criteria.getSort("city", "desc"));
		repository.findAll(criteria.getFilter(), criteria.getSort("id", "desc")).iterator().forEachRemaining(testLogger::info);
	}

	@Test
	public void findAllByCriteriaWithPagingAndSort() {
		RoleCriteria criteria = new RoleCriteria();
		criteria.setAppName("myapp-backend");
		Sort sort = criteria.getSort("id", "desc").and(criteria.getSort("name", "asc"));
		// Page<Role> results = repository.findAll(criteria.getFilter(), criteria.getPager(0, 1, sort), "Role(actions,administrators(content))");
		Page<Role> results = repository.findAll(criteria.getFilter(), criteria.getPager(0, 1, sort), "Role(actions)");

//		results.iterator().forEachRemaining(role -> {
//			testLogger.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx " + role.getName() + " xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//			role.getAdministrators().forEach(testLogger::info);
//		});

		results.iterator().forEachRemaining(testLogger::info);
	}
}
