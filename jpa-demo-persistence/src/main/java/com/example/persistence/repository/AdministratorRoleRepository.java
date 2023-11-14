package com.example.persistence.repository;

import com.example.persistence.criteria.AdministratorRoleCriteria;
import com.example.persistence.entity.AdministratorRole;
import com.example.persistence.entity.QAdministratorRole;
import com.example.persistence.repository.base.AbstractRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.stereotype.Repository;

import static com.example.persistence.config.PrimaryPersistenceContext.EM_FACTORY;

@Repository
public class AdministratorRoleRepository extends AbstractRepositoryImpl<AdministratorRole, AdministratorRoleCriteria, Long> {

	private final QAdministratorRole qEntity = QAdministratorRole.administratorRole;

	public AdministratorRoleRepository(@Qualifier(EM_FACTORY) EntityManager entityManager) {
		super(new JpaMetamodelEntityInformation<>(AdministratorRole.class, entityManager.getMetamodel(), entityManager.getEntityManagerFactory().getPersistenceUnitUtil()), entityManager);
	}
}
