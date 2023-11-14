package com.example.persistence.repository;

import com.example.persistence.criteria.LoginHistoryCriteria;
import com.example.persistence.entity.LoginHistory;
import com.example.persistence.entity.QLoginHistory;
import com.example.persistence.repository.base.AbstractRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.stereotype.Repository;

import static com.example.persistence.config.PrimaryPersistenceContext.EM_FACTORY;

@Repository
public class LoginHistoryRepository extends AbstractRepositoryImpl<LoginHistory, LoginHistoryCriteria, Long> {

	private final QLoginHistory qEntity = QLoginHistory.loginHistory;

	public LoginHistoryRepository(@Qualifier(EM_FACTORY) EntityManager entityManager) {
		super(new JpaMetamodelEntityInformation<>(LoginHistory.class, entityManager.getMetamodel(), entityManager.getEntityManagerFactory().getPersistenceUnitUtil()), entityManager);
	}
}
