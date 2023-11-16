package com.example.persistence.unitTests;

import com.example.persistence.CommonTestBase;
import com.example.persistence.criteria.AdministratorCriteria;
import com.example.persistence.dto.AuthenticatedClientDTO;
import com.example.persistence.entity.Administrator;
import com.example.persistence.repository.AdministratorRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdministratorUnitTest extends CommonTestBase {
	private static final Logger testLogger = LogManager.getLogger("testLogs." + AdministratorUnitTest.class.getName());

	@Autowired
	private AdministratorRepository repository;

	@Test
	public void findAll() {
		repository.findAll(new AdministratorCriteria().getFilter()).iterator().forEachRemaining(testLogger::info);
	}

	@Test
	public void findOneById() {
		Long findId = 1L;
		repository.findById(findId).ifPresent(testLogger::info);
	}

	@Test
	public void findOneByLoginId() {
		//repository.findByLoginId("superuser@1001", "Administrator(administratorRoles,staticContent)").ifPresent(testLogger::info);
		repository.findByLoginId("superuser@1001", "Administrator(roles)").ifPresent(testLogger::info);
	}

	@Test
	public void findOneByCriteria() {
		AdministratorCriteria criteria = new AdministratorCriteria();
		criteria.setId(1L);
		criteria.setLoginId("superuser@1001");
		criteria.setKeyword("user");
		repository.findOne(criteria.getFilter()).ifPresent(testLogger::info);
	}

	@Test
	public void findAllByCriteria() {
		AdministratorCriteria criteria = new AdministratorCriteria();
		criteria.setKeyword("user");
		repository.findAll(criteria.getFilter()).iterator().forEachRemaining(testLogger::info);
	}

	@Test
	public void findAllByCriteriaWithSort() {
		AdministratorCriteria criteria = new AdministratorCriteria();
		criteria.setKeyword("user");
		// multiple sorting
		// Sort sort = criteria.getSort("country", "asc").and(criteria.getSort("city", "desc"));
		repository.findAll(criteria.getFilter(), criteria.getSort("id", "desc")).iterator().forEachRemaining(testLogger::info);
	}

	@Test
	public void findAllByCriteriaWithPagingAndSort() {
		AdministratorCriteria criteria = new AdministratorCriteria();
		criteria.setKeyword("user");
		Sort sort = criteria.getSort("id", "desc").and(criteria.getSort("name", "asc"));
		repository.findAll(criteria.getFilter(), criteria.getPager(0, 20, sort)).iterator().forEachRemaining(testLogger::info);
	}

	@Test
	public void insertSingle() {
		Administrator entity = new Administrator();
		entity.setLoginId("sample_loginId");
		entity.setPassword("sample_password");
		entity.setName("sample_user");
		entity.setStatus(AuthenticatedClientDTO.Status.ACTIVE);
		entity.setRecordRegId(TEST_CREATE_USER_ID);
		entity.setRecordUpdId(TEST_UPDATE_USER_ID);

		entity = repository.save(entity);

		testLogger.info("Inserted ID # " + entity.getId());
	}

	@Test
	public void insertList() {
		List<Administrator> entityList = new ArrayList<>();

		Administrator entity1 = new Administrator();
		entity1.setLoginId("sample_loginId1");
		entity1.setPassword("sample_password1");
		entity1.setName("sample_user1");
		entity1.setStatus(AuthenticatedClientDTO.Status.ACTIVE);
		entity1.setRecordRegId(TEST_CREATE_USER_ID);
		entity1.setRecordUpdId(TEST_UPDATE_USER_ID);

		entityList.add(entity1);

		Administrator entity2 = new Administrator();
		entity2.setLoginId("sample_loginId2");
		entity2.setPassword("sample_password2");
		entity2.setName("sample_user2");
		entity2.setStatus(AuthenticatedClientDTO.Status.TEMPORARY);
		entity2.setRecordRegId(TEST_CREATE_USER_ID);
		entity2.setRecordUpdId(TEST_UPDATE_USER_ID);

		entityList.add(entity2);

		entityList = repository.saveAll(entityList);

		entityList.forEach(entity -> testLogger.info("Inserted Entity => " + entity));
	}

	@Test
	public void updateById() {
		Long updateId = 1L;
		repository.findById(updateId).ifPresent(oldEntity -> {
			testLogger.info("Old Entity Informations ==> " + oldEntity);
			oldEntity.setLoginId("sample_loginId1");
			oldEntity.setPassword("sample_password1");
			oldEntity.setName("sample_user1");
			oldEntity.setStatus(AuthenticatedClientDTO.Status.TEMPORARY);
			oldEntity.setRecordUpdId(TEST_UPDATE_USER_ID);
			//we don't need to flush if we don't fetch within a transaction to view
			repository.flush();

			repository.findById(oldEntity.getId()).ifPresent(testLogger::info);
		});
	}

	@Test
	public void updateByCriteria() {
		AdministratorCriteria criteria = new AdministratorCriteria();
		criteria.setId(1L);

		Administrator updateEntity = new Administrator();
		updateEntity.setName("Sample update name");

		long effectedRows = repository.update(updateEntity, criteria);
		testLogger.info("Effected Row Count ==> " + effectedRows);
		//we don't need to flush if we don't fetch within a transaction to view
		repository.flush();
		repository.findOne(criteria.getFilter()).ifPresent(testLogger::info);
	}

	//
	@Test
	public void deleteById() {
		Long deleteId = 1L;
		repository.deleteById(deleteId);
		repository.findById(deleteId).ifPresent(testLogger::info);
	}

	//
	@Test
	public void deleteByObject() {
		Long deleteObjectId = 1L;
		Optional<Administrator> deleteEntity = repository.findById(deleteObjectId);
		deleteEntity.ifPresent(repository::delete);
		repository.findById(deleteObjectId).ifPresent(testLogger::info);
	}

	@Test
	public void deleteByCriteria() {
		AdministratorCriteria criteria = new AdministratorCriteria();
		criteria.setId(1L);
		criteria.setName("Super User");
		criteria.setStatus(AuthenticatedClientDTO.Status.ACTIVE);

		long effectedRows = repository.deleteByCriteria(criteria);
		testLogger.info("Effected Row Count ==> " + effectedRows);
	}
}
