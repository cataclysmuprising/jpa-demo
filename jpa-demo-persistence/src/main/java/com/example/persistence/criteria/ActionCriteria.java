package com.example.persistence.criteria;

import com.example.persistence.entity.Action;
import com.example.persistence.entity.QAction;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString(callSuper = true)
public class ActionCriteria extends AbstractCriteria<QAction> {

	private String appName;

	private String page;

	private String actionName;

	private String displayName;

	private Action.ActionType actionType;

	private String url;

	private RoleCriteria role;

	@Override
	public Predicate getFilter() {
		QAction qEntity = QAction.action;
		return getFilter(qEntity);
	}

	@Override
	public Predicate getFilter(QAction qEntity) {
		BooleanBuilder predicate = getCommonFilter(qEntity._super);

		if (StringUtils.isNotBlank(appName)) {
			predicate.and(qEntity.appName.eq(appName));
		}
		if (StringUtils.isNotBlank(page)) {
			predicate.and(qEntity.page.eq(page));
		}
		if (StringUtils.isNotBlank(actionName)) {
			predicate.and(qEntity.actionName.eq(actionName));
		}
		if (StringUtils.isNotBlank(displayName)) {
			predicate.and(qEntity.displayName.eq(displayName));
		}
		if (actionType != null) {
			predicate.and(qEntity.actionType.eq(actionType));
		}
		if (StringUtils.isNotBlank(url)) {
			predicate.and(qEntity.url.eq(url));
		}

		// define for keyword search
		if (StringUtils.isNotBlank(keyword)) {
			// @formatter:off
			predicate.and(
					qEntity.appName.containsIgnoreCase(keyword)
							.or(qEntity.page.containsIgnoreCase(keyword))
							.or(qEntity.actionName.containsIgnoreCase(keyword))
							.or(qEntity.displayName.containsIgnoreCase(keyword))
							.or(qEntity.url.containsIgnoreCase(keyword))
							.or(qEntity.description.containsIgnoreCase(keyword))
			);
			// @formatter:on
		}

		if (role != null) {
			predicate.and(role.getFilter(qEntity.roles.any()));
		}

		return predicate;
	}
}

