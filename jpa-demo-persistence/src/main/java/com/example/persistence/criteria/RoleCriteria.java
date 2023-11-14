package com.example.persistence.criteria;

import com.example.persistence.entity.QRole;
import com.example.persistence.entity.Role;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString(callSuper = true)
public class RoleCriteria extends AbstractCriteria<QRole> {

	private String appName;

	private String name;

	private Role.RoleType roleType;

	private AdministratorCriteria administrator;

	private ActionCriteria action;

	@Override
	public Predicate getFilter() {
		QRole qEntity = QRole.role;
		return getFilter(qEntity);
	}

	@Override
	public Predicate getFilter(QRole qEntity) {
		BooleanBuilder predicate = getCommonFilter(qEntity._super);

		if (StringUtils.isNotBlank(appName)) {
			predicate.and(qEntity.appName.eq(appName));
		}
		if (StringUtils.isNotBlank(name)) {
			predicate.and(qEntity.name.eq(name));
		}
		if (roleType != null) {
			predicate.and(qEntity.roleType.eq(roleType));
		}

		// define for keyword search
		if (StringUtils.isNotBlank(keyword)) {
			// @formatter:off
			predicate.and(
					qEntity.appName.containsIgnoreCase(keyword)
							.or(qEntity.name.containsIgnoreCase(keyword))
							.or(qEntity.description.containsIgnoreCase(keyword))
			);
			// @formatter:on
		}

		if (administrator != null) {
			predicate.and(administrator.getFilter(qEntity.administrators.any()));
		}
		if (action != null) {
			predicate.and(action.getFilter(qEntity.actions.any()));
		}

		return predicate;
	}
}

