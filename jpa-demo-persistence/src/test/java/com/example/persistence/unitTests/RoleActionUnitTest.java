package com.example.persistence.unitTests;

import com.example.persistence.CommonTestBase;
import com.example.persistence.criteria.RoleActionCriteria;
import com.example.persistence.entity.RoleAction;
import com.example.persistence.repository.RoleActionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.testng.annotations.Test;

public class RoleActionUnitTest extends CommonTestBase {
	private static final Logger testLogger = LogManager.getLogger("testLogs." + RoleActionUnitTest.class.getName());

	@Autowired
	private RoleActionRepository repository;

	@Test
	public void findAll() {
		repository.findAll(new RoleActionCriteria().getFilter()).iterator().forEachRemaining(testLogger::info);
	}

	@Test
	public void findAllByCriteriaWithPagingAndSort() {
		RoleActionCriteria criteria = new RoleActionCriteria();
		Sort sort = criteria.getSort("roleId", "asc");
		repository.findAll(criteria.getFilter(), criteria.getPager(0, 100, sort), "RoleAction(role,action)").iterator().forEachRemaining(testLogger::info);
	}

	@Test
	public void insertSingle() {
		RoleAction entity = new RoleAction();
		entity.setRoleId(3L);
		entity.setActionId(10031L);
		entity.setRecordRegId(TEST_CREATE_USER_ID);
		entity.setRecordUpdId(TEST_UPDATE_USER_ID);

		entity = repository.save(entity);
		testLogger.info("Inserted ID # " + entity.getId());
	}
}
