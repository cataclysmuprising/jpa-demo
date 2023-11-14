package com.example.persistence.criteria;

import com.example.persistence.dto.AuthenticatedClientDTO;
import com.example.persistence.entity.QAdministrator;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString(callSuper = true)
public class AdministratorCriteria extends AbstractCriteria<QAdministrator> {
	private Long contentId;
	private String name;
	private String loginId;
	private AuthenticatedClientDTO.Status status;

	private RoleCriteria role;
	private StaticContentCriteria staticContent;

	@Override
	public Predicate getFilter() {
		QAdministrator qEntity = QAdministrator.administrator;
		return getFilter(qEntity);
	}

	@Override
	public Predicate getFilter(QAdministrator qEntity) {
		BooleanBuilder predicate = getCommonFilter(qEntity._super);

		if (contentId != null) {
			predicate.and(qEntity.contentId.eq(contentId));
		}
		if (StringUtils.isNotBlank(name)) {
			predicate.and(qEntity.name.eq(name));
		}
		if (StringUtils.isNotBlank(loginId)) {
			predicate.and(qEntity.loginId.eq(loginId));
		}
		if (status != null) {
			predicate.and(qEntity.status.eq(status));
		}

		// define for keyword search
		if (StringUtils.isNotBlank(keyword)) {
			// @formatter:off
			predicate.and(
					qEntity.name.containsIgnoreCase(keyword)
							.or(qEntity.loginId.containsIgnoreCase(keyword))
			);
			// @formatter:on
		}
		if (role != null) {
			predicate.and(role.getFilter(qEntity.roles.any()));
		}
		if (staticContent != null) {
			predicate.and(staticContent.getFilter());
		}

		return predicate;
	}
}

