package com.example.persistence.criteria;

import com.example.persistence.entity.QStaticContent;
import com.example.persistence.entity.StaticContent;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString(callSuper = true)
public class StaticContentCriteria extends AbstractCriteria<QStaticContent> {
	private String fileName;

	private String filePath;

	private String fileSize;

	private StaticContent.FileType fileType;

	@Override
	public Predicate getFilter() {
		QStaticContent qEntity = QStaticContent.staticContent;
		return getFilter(qEntity);
	}

	@Override
	public Predicate getFilter(QStaticContent qEntity) {
		BooleanBuilder predicate = getCommonFilter(qEntity._super);

		if (StringUtils.isNotBlank(fileName)) {
			predicate.and(qEntity.fileName.eq(fileName));
		}
		if (StringUtils.isNotBlank(filePath)) {
			predicate.and(qEntity.filePath.eq(filePath));
		}
		if (StringUtils.isNotBlank(fileSize)) {
			predicate.and(qEntity.fileSize.eq(fileSize));
		}
		if (fileType != null) {
			predicate.and(qEntity.fileType.eq(fileType));
		}

		// define for keyword search
		if (StringUtils.isNotBlank(keyword)) {
			// @formatter:off
			predicate.and(
					qEntity.fileName.containsIgnoreCase(keyword)
							.or(qEntity.filePath.containsIgnoreCase(keyword))
							.or(qEntity.fileSize.containsIgnoreCase(keyword))
			);
			// @formatter:on
		}
		return predicate;
	}
}

