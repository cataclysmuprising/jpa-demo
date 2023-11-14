package com.example.persistence.serviceTests;

import com.example.persistence.CommonTestBase;
import com.example.persistence.criteria.AdministratorCriteria;
import com.example.persistence.criteria.RoleCriteria;
import com.example.persistence.criteria.StaticContentCriteria;
import com.example.persistence.dto.AdministratorDTO;
import com.example.persistence.dto.AuthenticatedClientDTO;
import com.example.persistence.mapper.utils.PaginatedResult;
import com.example.persistence.service.AdministratorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class AdministratorServiceTest extends CommonTestBase {
	private static final Logger testLogger = LogManager.getLogger("testLogs." + AdministratorServiceTest.class.getName());

	@Autowired
	private AdministratorService service;

	@Test
	public void findById() {
		AdministratorDTO dto = service.findById(1L);
		testLogger.info("Administrator ==> " + dto);
	}

	@Test
	public void findOne() {
		AdministratorCriteria criteria = new AdministratorCriteria();
		criteria.setName("Super User");

		StaticContentCriteria contentCriteria = new StaticContentCriteria();
		contentCriteria.setFileName("testfile");
		criteria.setStaticContent(contentCriteria);

		RoleCriteria roleCriteria = new RoleCriteria();
		roleCriteria.setAppName("myapp-backend");
		criteria.setRole(roleCriteria);

		Sort sort = criteria.getSort("id", "desc").and(criteria.getSort("name", "asc"));
		AdministratorDTO dto = service.findOne(criteria, "Administrator(roles,staticContent)");
		testLogger.info("Administrator ==> " + dto);
		testLogger.info("Roles ==> " + dto.getRoles());
		testLogger.info("StaticContent ==> " + dto.getStaticContent());
	}

	@Test
	public void findAll() {
		AdministratorCriteria criteria = new AdministratorCriteria();
		criteria.setName("Super User");

		StaticContentCriteria contentCriteria = new StaticContentCriteria();
		contentCriteria.setFileName("testfile");
		criteria.setStaticContent(contentCriteria);

		RoleCriteria roleCriteria = new RoleCriteria();
		roleCriteria.setAppName("myapp-backend");
		criteria.setRole(roleCriteria);

		Sort sort = criteria.getSort("id", "desc").and(criteria.getSort("name", "asc"));
		List<AdministratorDTO> results = service.findAll(criteria, "Administrator(roles,staticContent)");
		results.forEach(result -> {
			testLogger.info("Administrator ==> " + result);
			testLogger.info("Roles ==> " + result.getRoles());
			testLogger.info("StaticContent ==> " + result.getStaticContent());
		});
	}

	@Test
	public void findAllWithPager() {
		AdministratorCriteria criteria = new AdministratorCriteria();
		criteria.setName("Super User");

		StaticContentCriteria contentCriteria = new StaticContentCriteria();
		contentCriteria.setFileName("testfile");
		criteria.setStaticContent(contentCriteria);

		RoleCriteria roleCriteria = new RoleCriteria();
		roleCriteria.setAppName("myapp-backend");
		criteria.setRole(roleCriteria);

		Sort sort = criteria.getSort("id", "desc").and(criteria.getSort("name", "asc"));
		PaginatedResult<AdministratorDTO> result = service.findAll(criteria, criteria.getPager(0, 20, sort), "Administrator(roles,staticContent)");

		testLogger.info("Result ==> " + result);
		if (!CollectionUtils.isEmpty(result.getData())) {
			result.getData().forEach(data -> {
				testLogger.info("Administrator ==> " + data);
				testLogger.info("Roles ==> " + data.getRoles());
				testLogger.info("StaticContent ==> " + data.getStaticContent());
			});
		}
	}

	@Test
	public void create() {
		AdministratorDTO dto = new AdministratorDTO();
		dto.setLoginId("sample_loginId");
		dto.setPassword("sample_password");
		dto.setName("sample_user");
		dto.setStatus(AuthenticatedClientDTO.Status.ACTIVE);

		dto = service.create(dto, TEST_CREATE_USER_ID);

		testLogger.info("Inserted DTO # " + dto);
	}

	@Test
	public void createAll() {
		List<AdministratorDTO> dtos = new ArrayList<>();

		AdministratorDTO dto1 = new AdministratorDTO();
		dto1.setLoginId("sample_loginId1");
		dto1.setPassword("sample_password1");
		dto1.setName("sample_user1");
		dto1.setStatus(AuthenticatedClientDTO.Status.ACTIVE);

		dtos.add(dto1);

		AdministratorDTO dto2 = new AdministratorDTO();
		dto2.setLoginId("sample_loginId2");
		dto2.setPassword("sample_password2");
		dto2.setName("sample_user2");
		dto2.setStatus(AuthenticatedClientDTO.Status.TEMPORARY);

		dtos.add(dto2);
		service.createAll(dtos, TEST_CREATE_USER_ID);
	}

	@Test
	public void updateByCriteria() {
		AdministratorCriteria criteria = new AdministratorCriteria();
		criteria.setName("Super User");

		AdministratorDTO updateDTO = new AdministratorDTO();
		updateDTO.setLoginId("sample_loginId1");
		updateDTO.setPassword("sample_password1");
		updateDTO.setName("sample_user1");
		updateDTO.setStatus(AuthenticatedClientDTO.Status.TEMPORARY);
		long effectedCounts = service.update(updateDTO, criteria, TEST_UPDATE_USER_ID);
		testLogger.info("Total effected counts ==> " + effectedCounts);
	}

	@Test
	public void deleteById() {
		long deleteId = 1L;
		service.deleteById(deleteId);
	}

	@Test
	public void deleteByCriteria() {
		AdministratorCriteria criteria = new AdministratorCriteria();
		criteria.setId(1L);
		criteria.setName("Super User");
		criteria.setStatus(AuthenticatedClientDTO.Status.ACTIVE);

		long effectedRows = service.delete(criteria);
		testLogger.info("Effected Row Count ==> " + effectedRows);
	}
}
