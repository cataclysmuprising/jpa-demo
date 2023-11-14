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
}
