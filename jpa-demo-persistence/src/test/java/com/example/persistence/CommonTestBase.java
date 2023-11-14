package com.example.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.Collection;

//@Rollback(value = false) // uncomment for permanent persist
@ActiveProfiles("dev")
@SpringBootTest(classes = PersistenceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CommonTestBase extends AbstractTransactionalTestNGSpringContextTests {
	protected static final long TEST_CREATE_USER_ID = 10009L;
	protected static final long TEST_UPDATE_USER_ID = 90001L;
	private static final Logger testLogger = LogManager.getLogger("testLogs." + CommonTestBase.class.getName());

	@BeforeMethod
	public void beforeMethod(Method method) {
		testLogger.info("***** Unit-TEST : Testing method '" + method.getName() + "' has started. *****");
		//MockitoAnnotations.openMocks(this); // This could be pulled up into a shared base class
	}

	@AfterMethod
	public void afterMethod(Method method) {
		testLogger.info("----- Unit-TEST : Testing method '" + method.getName() + "' has finished. -----");
	}

	protected <T> void showEntriesOfCollection(Collection<T> collection) {
		if (collection != null) {
			for (Object obj : collection) {
				testLogger.info(" >>> " + obj.toString());
			}
		}
	}
}
