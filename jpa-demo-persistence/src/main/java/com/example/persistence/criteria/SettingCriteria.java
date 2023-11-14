package com.example.persistence.criteria;

import com.example.persistence.entity.QSetting;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString(callSuper = true)
public class SettingCriteria extends AbstractCriteria<QSetting> {
	private String group;

	private String subGroup;

	private String type;

	private String name;

	private String value;

	@Override
	public Predicate getFilter() {
		QSetting qEntity = QSetting.setting;
		return getFilter(qEntity);
	}

	@Override
	public Predicate getFilter(QSetting qEntity) {
		BooleanBuilder predicate = getCommonFilter(qEntity._super);

		if (StringUtils.isNotBlank(group)) {
			predicate.and(qEntity.group.eq(group));
		}
		if (StringUtils.isNotBlank(subGroup)) {
			predicate.and(qEntity.subGroup.eq(subGroup));
		}
		if (StringUtils.isNotBlank(type)) {
			predicate.and(qEntity.type.eq(type));
		}
		if (StringUtils.isNotBlank(name)) {
			predicate.and(qEntity.name.eq(name));
		}
		if (StringUtils.isNotBlank(value)) {
			predicate.and(qEntity.value.eq(value));
		}

		// define for keyword search
		if (StringUtils.isNotBlank(keyword)) {
			// @formatter:off
			predicate.and(
					qEntity.group.containsIgnoreCase(keyword)
							.or(qEntity.subGroup.containsIgnoreCase(keyword))
							.or(qEntity.type.containsIgnoreCase(keyword))
							.or(qEntity.name.containsIgnoreCase(keyword))
							.or(qEntity.value.containsIgnoreCase(keyword))
			);
			// @formatter:on
		}

		return predicate;
	}
}

