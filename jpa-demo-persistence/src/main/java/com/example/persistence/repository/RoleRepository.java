package com.example.persistence.repository;

import com.example.persistence.criteria.RoleCriteria;
import com.example.persistence.entity.QRole;
import com.example.persistence.entity.Role;
import com.example.persistence.repository.base.AbstractRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.persistence.config.PrimaryPersistenceContext.EM_FACTORY;

@Repository
public class RoleRepository extends AbstractRepositoryImpl<Role, RoleCriteria, Long> {

	private final QRole qEntity = QRole.role;

	public RoleRepository(@Qualifier(EM_FACTORY) EntityManager entityManager) {
		super(new JpaMetamodelEntityInformation<>(Role.class, entityManager.getMetamodel(), entityManager.getEntityManagerFactory().getPersistenceUnitUtil()), entityManager);
	}

	public List<String> selectRolesByActionURL(String actionUrl, String appName) {
		try {

			//@formatter:off
			String query = "SELECT DISTINCT "+
			" rol.name AS rol_name "+
			" FROM mjr_action act " +
			" INNER JOIN mjr_role_x_action ra ON ra.action_id = act.id " +
			" INNER JOIN mjr_role rol ON rol.id = ra.role_id " +
			" WHERE act.app_name = :appName " +
			" AND :actionUrl ~ act.url ";

			Query appliedQuery = entityManager.createNativeQuery(query)
					.setParameter("actionUrl", actionUrl)
					.setParameter("appName", appName);

			//@formatter:on
			
			return appliedQuery.getResultList();
		}
		catch (Exception ex) {
			throw new IncorrectResultSizeDataAccessException(ex.getMessage(), 1, ex);
		}
	}
}
