package com.example.persistence.criteria;

import com.example.persistence.entity.QAdministratorRole;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class AdministratorRoleCriteria extends AbstractCriteria<QAdministratorRole> {

	private Long administratorId;

	private Long roleId;

	private AdministratorCriteria administrator;

	private RoleCriteria role;

	@Override
	public Predicate getFilter() {
		QAdministratorRole qEntity = QAdministratorRole.administratorRole;
		return getFilter(qEntity);
	}

	@Override
	public Predicate getFilter(QAdministratorRole qEntity) {
		BooleanBuilder predicate = getCommonFilter(qEntity._super);

		if (administratorId != null) {
			predicate.and(qEntity.administrator.id.eq(administratorId));
		}
		if (roleId != null) {
			predicate.and(qEntity.role.id.eq(roleId));
		}

		if (administrator != null) {
			predicate.and(administrator.getFilter());
		}
		if (role != null) {
			predicate.and(role.getFilter());
		}

		return predicate;
	}
}

