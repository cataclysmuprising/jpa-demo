package com.example.persistence.repository;

import com.example.persistence.criteria.ActionCriteria;
import com.example.persistence.entity.Action;
import com.example.persistence.entity.QAction;
import com.example.persistence.repository.base.AbstractRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.stereotype.Repository;

import static com.example.persistence.config.PrimaryPersistenceContext.EM_FACTORY;

@Repository
public class ActionRepository extends AbstractRepositoryImpl<Action, ActionCriteria, Long> {

	private final QAction qEntity = QAction.action;

	public ActionRepository(@Qualifier(EM_FACTORY) EntityManager entityManager) {
		super(new JpaMetamodelEntityInformation<>(Action.class, entityManager.getMetamodel(), entityManager.getEntityManagerFactory().getPersistenceUnitUtil()), entityManager);
	}

//	public List<String> selectAvailableActionsForUser(String page, String appName, List<Long> roleIds) throws BusinessException {
//		try {
//			QRole role = QRole.role;
//			QAction action = QAction.action;
//			QRoleAction roleAction = QRoleAction.roleAction;
//			//@formatter:off
//			return queryFactory.select(action.actionName)
//					.from(action)
//					.leftJoin(roleAction).on(roleAction.actionId.eq(action.id))
//					.leftJoin(role).on(role.id.eq(roleAction.roleId))
//					.where(action.appName.eq(appName)
//							.and(action.actionType == Action.ActionType.))
//					.fetch();
//
//		}
//		catch (Exception e) {
//			throw new BusinessException(e.getMessage(),e);
//		}
//	}
}
