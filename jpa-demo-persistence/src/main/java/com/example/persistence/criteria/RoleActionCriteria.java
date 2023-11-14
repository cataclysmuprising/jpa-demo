package com.example.persistence.criteria;

import com.example.persistence.entity.QRoleAction;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class RoleActionCriteria extends AbstractCriteria<QRoleAction> {

	private Long roleId;

	private Long actionId;

	private RoleCriteria role;

	private ActionCriteria action;

	@Override
	public Predicate getFilter() {
		QRoleAction qEntity = QRoleAction.roleAction;
		return getFilter(qEntity);
	}

	@Override
	public Predicate getFilter(QRoleAction qEntity) {
		BooleanBuilder predicate = getCommonFilter(qEntity._super);

		if (roleId != null) {
			predicate.and(qEntity.role.id.eq(roleId));
		}
		if (actionId != null) {
			predicate.and(qEntity.action.id.eq(actionId));
		}

		if (role != null) {
			predicate.and(role.getFilter());
		}
		if (action != null) {
			predicate.and(action.getFilter());
		}

		return predicate;
	}
}

