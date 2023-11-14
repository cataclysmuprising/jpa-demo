package com.example.persistence.repository;

import com.example.persistence.criteria.RoleCriteria;
import com.example.persistence.entity.QRole;
import com.example.persistence.entity.Role;
import com.example.persistence.repository.base.AbstractRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.stereotype.Repository;

import static com.example.persistence.config.PrimaryPersistenceContext.EM_FACTORY;

@Repository
public class RoleRepository extends AbstractRepositoryImpl<Role, RoleCriteria, Long> {

	private final QRole qEntity = QRole.role;

	public RoleRepository(@Qualifier(EM_FACTORY) EntityManager entityManager) {
		super(new JpaMetamodelEntityInformation<>(Role.class, entityManager.getMetamodel(), entityManager.getEntityManagerFactory().getPersistenceUnitUtil()), entityManager);
	}
}
