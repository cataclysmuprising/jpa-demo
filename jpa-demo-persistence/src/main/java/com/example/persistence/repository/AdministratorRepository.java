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

//	public Optional<AuthenticatedClientDTO> selectAuthenticatedClient(String loginId) {
//		try {
//			QRole role = QRole.role;
//			QAdministrator admin = QAdministrator.administrator;
//			QAdministratorRole adminRole = QAdministratorRole.administratorRole;
//			//@formatter:off
//			Tuple tuple = queryFactory.select(admin.id, admin.name, admin.loginId,
//							admin.password, admin.contentId, admin.status,
//							Expressions.constant(0)
//
//					)
//
//					.from(admin)
//					.leftJoin(adminRole).on(adminRole.administratorId.eq(admin.id))
//					.leftJoin(role).on(role.id.eq(adminRole.roleId))
//					.where(admin.loginId.eq(loginId))
//					.groupBy(admin.id)
//					.fetchOne();
//			//@formatter:off
//
//		}
//		catch (NonUniqueResultException ex) {
//			throw new IncorrectResultSizeDataAccessException(ex.getMessage(), 1, ex);
//		} return Optional.empty();
//	}
}
