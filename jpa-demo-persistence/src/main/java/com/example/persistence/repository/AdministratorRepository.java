package com.example.persistence.repository;

import com.example.persistence.criteria.AdministratorCriteria;
import com.example.persistence.entity.Administrator;
import com.example.persistence.entity.QAdministrator;
import com.example.persistence.repository.base.AbstractRepositoryImpl;
import com.querydsl.core.NonUniqueResultException;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.example.persistence.config.PrimaryPersistenceContext.EM_FACTORY;

@Repository
public class AdministratorRepository extends AbstractRepositoryImpl<Administrator, AdministratorCriteria, Long> {

	private final QAdministrator qEntity = QAdministrator.administrator;

	public AdministratorRepository(@Qualifier(EM_FACTORY) EntityManager entityManager) {
		super(new JpaMetamodelEntityInformation<>(Administrator.class, entityManager.getMetamodel(), entityManager.getEntityManagerFactory().getPersistenceUnitUtil()), entityManager);
	}

	public Optional<Administrator> findByLoginId(String loginId, String... hints) {
		try {
			return Optional.ofNullable(doCreateQuery(getRelatedDataHints(hints), qEntity.loginId.eq(loginId)).select(qEntity).fetchOne());
		}
		catch (NonUniqueResultException ex) {
			throw new IncorrectResultSizeDataAccessException(ex.getMessage(), 1, ex);
		}
	}
}
