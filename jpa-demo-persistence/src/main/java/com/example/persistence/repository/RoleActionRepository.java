package com.example.persistence.repository;

import com.example.persistence.criteria.RoleActionCriteria;
import com.example.persistence.entity.QRoleAction;
import com.example.persistence.entity.RoleAction;
import com.example.persistence.repository.base.AbstractRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.stereotype.Repository;

import static com.example.persistence.config.PrimaryPersistenceContext.EM_FACTORY;

@Repository
public class RoleActionRepository extends AbstractRepositoryImpl<RoleAction, RoleActionCriteria, Long> {

	private final QRoleAction qEntity = QRoleAction.roleAction;

	public RoleActionRepository(@Qualifier(EM_FACTORY) EntityManager entityManager) {
		super(new JpaMetamodelEntityInformation<>(RoleAction.class, entityManager.getMetamodel(), entityManager.getEntityManagerFactory().getPersistenceUnitUtil()), entityManager);
	}
}
