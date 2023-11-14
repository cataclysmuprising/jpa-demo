package com.example.persistence.unitTests;

import com.example.persistence.CommonTestBase;
import com.example.persistence.criteria.AdministratorRoleCriteria;
import com.example.persistence.repository.AdministratorRoleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class AdministratorRoleUnitTest extends CommonTestBase {
	private static final Logger testLogger = LogManager.getLogger("testLogs." + AdministratorRoleUnitTest.class.getName());

	@Autowired
	private AdministratorRoleRepository repository;

	@Test
	public void findAll() {
		repository.findAll(new AdministratorRoleCriteria().getFilter()).iterator().forEachRemaining(testLogger::info);
	}
}
