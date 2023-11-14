package com.example.persistence.repository;

import com.example.persistence.criteria.StaticContentCriteria;
import com.example.persistence.entity.QStaticContent;
import com.example.persistence.entity.StaticContent;
import com.example.persistence.repository.base.AbstractRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.stereotype.Repository;

import static com.example.persistence.config.PrimaryPersistenceContext.EM_FACTORY;

@Repository
public class StaticContentRepository extends AbstractRepositoryImpl<StaticContent, StaticContentCriteria, Long> {

	private final QStaticContent qEntity = QStaticContent.staticContent;

	public StaticContentRepository(@Qualifier(EM_FACTORY) EntityManager entityManager) {
		super(new JpaMetamodelEntityInformation<>(StaticContent.class, entityManager.getMetamodel(), entityManager.getEntityManagerFactory().getPersistenceUnitUtil()), entityManager);
	}
}
