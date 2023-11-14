package com.example.persistence.repository;

import com.example.persistence.criteria.SettingCriteria;
import com.example.persistence.entity.QSetting;
import com.example.persistence.entity.Setting;
import com.example.persistence.repository.base.AbstractRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.stereotype.Repository;

import static com.example.persistence.config.PrimaryPersistenceContext.EM_FACTORY;

@Repository
public class SettingRepository extends AbstractRepositoryImpl<Setting, SettingCriteria, Long> {

	private final QSetting qEntity = QSetting.setting;

	public SettingRepository(@Qualifier(EM_FACTORY) EntityManager entityManager) {
		super(new JpaMetamodelEntityInformation<>(Setting.class, entityManager.getMetamodel(), entityManager.getEntityManagerFactory().getPersistenceUnitUtil()), entityManager);
	}
}
