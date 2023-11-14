package com.example.persistence.criteria;

import com.example.persistence.dto.AuthenticatedClientDTO;
import com.example.persistence.entity.QLoginHistory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class LoginHistoryCriteria extends AbstractCriteria<QLoginHistory> {

	private Long clientId;

	private AuthenticatedClientDTO.ClientType clientType;

	private String ipAddress;

	private String os;

	private String clientAgent;

	private LocalDateTime loginDateFrom;

	private LocalDateTime loginDateTo;

	@Override
	public Predicate getFilter() {
		QLoginHistory qEntity = QLoginHistory.loginHistory;
		return getFilter(qEntity);
	}

	@Override
	public Predicate getFilter(QLoginHistory qEntity) {
		BooleanBuilder predicate = getCommonFilter(qEntity._super);

		if (clientId != null) {
			predicate.and(qEntity.clientId.eq(clientId));
		}
		if (clientType != null) {
			predicate.and(qEntity.clientType.eq(clientType));
		}
		if (StringUtils.isNotBlank(ipAddress)) {
			predicate.and(qEntity.ipAddress.eq(ipAddress));
		}
		if (StringUtils.isNotBlank(os)) {
			predicate.and(qEntity.os.eq(os));
		}
		if (StringUtils.isNotBlank(clientAgent)) {
			predicate.and(qEntity.clientAgent.eq(clientAgent));
		}
		if (loginDateFrom != null) {
			predicate.and(qEntity.loginDate.after(loginDateFrom));
		}
		if (loginDateTo != null) {
			predicate.and(qEntity.loginDate.before(loginDateTo));
		}

		// define for keyword search
		if (StringUtils.isNotBlank(keyword)) {
			// @formatter:off
			predicate.and(
					qEntity.ipAddress.containsIgnoreCase(keyword)
							.or(qEntity.os.containsIgnoreCase(keyword))
							.or(qEntity.clientAgent.containsIgnoreCase(keyword))
			);
			// @formatter:on
		}

		return predicate;
	}
}

