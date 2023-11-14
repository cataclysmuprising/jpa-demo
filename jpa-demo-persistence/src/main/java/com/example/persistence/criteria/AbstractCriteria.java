package com.example.persistence.criteria;

import com.example.persistence.entity.QAbstractEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Data
@ToString(exclude = {"DEFAULT_MAX_ROWS"})
public abstract class AbstractCriteria<Q> {
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private final int DEFAULT_MAX_ROWS = 100;

	protected String keyword;
	protected Long id;
	protected Set<Long> includeIds;
	protected Set<Long> excludeIds;
	protected String sortProperty;
	protected String sortType;
	private Long recordRegId;
	private Long recordUpdId;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime recordRegDateFrom;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime recordRegDateTo;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime recordUpdDateFrom;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime recordUpdDateTo;

	BooleanBuilder getCommonFilter(QAbstractEntity entity) {
		BooleanBuilder predicate = new BooleanBuilder();
		if (id != null) {
			predicate.and(entity.id.eq(id));
		}
		if (recordRegId != null) {
			predicate.and(entity.recordRegId.eq(recordRegId));
		}
		if (recordUpdId != null) {
			predicate.and(entity.recordUpdId.eq(recordUpdId));
		}
		if (recordRegDateFrom != null) {
			predicate.and(entity.recordRegDate.after(recordRegDateFrom));
		}
		if (recordRegDateTo != null) {
			predicate.and(entity.recordRegDate.before(recordRegDateTo));
		}
		if (recordUpdDateFrom != null) {
			predicate.and(entity.recordUpdDate.after(recordUpdDateFrom));
		}
		if (recordUpdDateTo != null) {
			predicate.and(entity.recordUpdDate.before(recordUpdDateTo));
		}
		if (!CollectionUtils.isEmpty(includeIds)) {
			predicate.and(entity.id.in(includeIds));
		}
		if (!CollectionUtils.isEmpty(excludeIds)) {
			predicate.and(entity.id.notIn(excludeIds));
		}
		return predicate;
	}

	public Pageable getPager(Integer offset, Integer limit) {
		if (offset == null || limit == null) {
			return null;
		}
		int page = offset > 0 ? offset / limit : 0;
		limit = limit > 0 ? limit : DEFAULT_MAX_ROWS;

		if (StringUtils.isBlank(sortProperty)) {
			return PageRequest.of(page, limit);
		}
		else {
			if (StringUtils.isBlank(sortType)) {
				sortType = "ASC";
			}
			Sort.Direction direction = ASC.toString().equalsIgnoreCase(sortType) ? ASC : DESC;
			return PageRequest.of(page, limit, Sort.by(Order.by(sortProperty).with(direction)));
		}
	}

	public Pageable getPager(Integer offset, Integer limit, Sort sort) {
		if (offset == null || limit == null) {
			return null;
		}
		int page = offset > 0 ? offset / limit : 0;
		limit = limit > 0 ? limit : DEFAULT_MAX_ROWS;

		if (sort == null) {
			return PageRequest.of(page, limit);
		}
		else {
			return PageRequest.of(page, limit, sort);
		}
	}

	public Pageable getPager(Integer offset, Integer limit, String sortProperty, String sortType) {
		if (offset == null || limit == null) {
			return null;
		}
		int page = offset > 0 ? offset / limit : 0;
		limit = limit > 0 ? limit : DEFAULT_MAX_ROWS;

		if (StringUtils.isBlank(sortProperty) || StringUtils.isBlank(sortType)) {
			return PageRequest.of(page, limit);
		}
		else {
			return PageRequest.of(page, limit, getSort(sortProperty, sortType));
		}
	}

	public Sort getSort(String sortProperty, String sortType) {
		if (StringUtils.isBlank(sortType)) {
			sortType = "ASC";
		}
		Sort.Direction direction = ASC.toString().equalsIgnoreCase(sortType) ? ASC : DESC;
		return Sort.by(Order.by(sortProperty).with(direction));
	}

	public abstract Predicate getFilter();

	public abstract Predicate getFilter(Q qEntity);
}
