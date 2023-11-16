package com.example.persistence.repository.base;

import com.example.persistence.criteria.AbstractCriteria;
import com.example.persistence.entity.AbstractEntity;
import com.example.persistence.entity.QAbstractEntity;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.AbstractJPAQuery;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.*;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.QSort;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractRepositoryImpl<T extends AbstractEntity, C extends AbstractCriteria<?>, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements AbstractRepository<T, C, ID> {
	protected final EntityManager entityManager;
	protected final EntityPath<T> path;
	protected final Querydsl querydsl;
	protected final JPAQueryFactory queryFactory;

	private final QuerydslJpaPredicateExecutor<T> querydslJpaPredicateExecutor;

	public AbstractRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
		this(entityInformation, entityManager, SimpleEntityPathResolver.INSTANCE);
	}

	public AbstractRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager, EntityPathResolver resolver) {
		super(entityInformation, entityManager);

		path = resolver.createPath(entityInformation.getJavaType());
		PathBuilder<T> builder = new PathBuilder<>(path.getType(), path.getMetadata());
		querydsl = new Querydsl(entityManager, builder);
		this.entityManager = entityManager;

		querydslJpaPredicateExecutor = new QuerydslJpaPredicateExecutor<>(entityInformation, entityManager, resolver, null);
		queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public List<Long> findIds(QAbstractEntity entity, Predicate predicate) {
		return createQuery(predicate).select(entity.id).fetch();
	}

	@Override
	public List<Long> findIds(QAbstractEntity entity, Predicate predicate, Sort sort) {
		return querydsl.applySorting(sort, createQuery(predicate).select(entity.id)).fetch();
	}

	@Override
	@Nonnull
	public Optional<T> findOne(@Nonnull Predicate predicate) {
		return querydslJpaPredicateExecutor.findOne(predicate);
	}

	@Override
	public Optional<T> findOne(Predicate predicate, String... hints) {
		try {
			return Optional.ofNullable(doCreateQuery(getRelatedDataHints(hints), predicate).select(path).fetchOne());
		}
		catch (NonUniqueResultException ex) {
			throw new IncorrectResultSizeDataAccessException(ex.getMessage(), 1, ex);
		}
	}

	@Override
	@Nonnull
	public <S extends T, R> R findBy(@Nonnull Predicate predicate, @Nonnull Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
		return querydslJpaPredicateExecutor.findBy(predicate, queryFunction);
	}

	@Override
	@Nonnull
	public List<T> findAll(@Nonnull Predicate predicate) {
		return querydslJpaPredicateExecutor.findAll(predicate);
	}

	@Override
	@Nonnull
	public List<T> findAll(@Nonnull Predicate predicate, @Nonnull OrderSpecifier<?>... orders) {
		return querydslJpaPredicateExecutor.findAll(predicate, orders);
	}

	@Override
	@Nonnull
	public List<T> findAll(@Nonnull Predicate predicate, @Nonnull Sort sort) {
		return querydslJpaPredicateExecutor.findAll(predicate, sort);
	}

	@Override
	@Nonnull
	public List<T> findAll(@Nonnull OrderSpecifier<?>... orders) {
		return querydslJpaPredicateExecutor.findAll(orders);
	}

	@Override
	@Nonnull
	public Page<T> findAll(@Nonnull Predicate predicate, @Nonnull Pageable pageable) {
		return querydslJpaPredicateExecutor.findAll(predicate, pageable);
	}

	@Override
	public List<T> findAll(Predicate predicate, String... hints) {
		JPQLQuery<?> countQuery = createCountQuery(predicate);
		JPQLQuery<T> query = doCreateQuery(getRelatedDataHints(hints), predicate).select(path);

		return query.select(path).fetch();
	}

	@Override
	public Page<T> findAll(Predicate predicate, Pageable pageable, String... hints) {
		Assert.notNull(pageable, "Pageable must not be null!");
		JPQLQuery<?> countQuery = createCountQuery(predicate);
		JPQLQuery<T> query = querydsl.applyPagination(pageable, doCreateQuery(getRelatedDataHints(hints), predicate).select(path));

		return PageableExecutionUtils.getPage(query.fetch(), pageable, countQuery::fetchCount);
	}

	@Override
	public long count(@Nonnull Predicate predicate) {
		return querydslJpaPredicateExecutor.count(predicate);
	}

	@Override
	public boolean exists(@Nonnull Predicate predicate) {
		return querydslJpaPredicateExecutor.exists(predicate);
	}

	protected JPQLQuery<?> createQuery(Predicate... predicate) {

		AbstractJPAQuery<?, ?> query = doCreateQuery(getQueryHints().withFetchGraphs(entityManager), predicate);

		CrudMethodMetadata metadata = getRepositoryMethodMetadata();

		if (metadata == null) {
			return query;
		}

		LockModeType type = metadata.getLockModeType();
		return type == null ? query : query.setLockMode(type);
	}

	protected JPQLQuery<?> createCountQuery(@Nullable Predicate... predicate) {
		return doCreateQuery(getQueryHints(), predicate);
	}

	protected AbstractJPAQuery<?, ?> doCreateQuery(QueryHints hints, @Nullable Predicate... predicate) {

		AbstractJPAQuery<?, ?> query = querydsl.createQuery(path);

		if (predicate != null) {
			query = query.where(predicate);
		}
		if (hints != null) {
			hints.forEach(query::setHint);
		}
		return query;
	}

	private List<T> executeSorted(JPQLQuery<T> query, OrderSpecifier<?>... orders) {
		return executeSorted(query, new QSort(orders));
	}

	private List<T> executeSorted(JPQLQuery<T> query, Sort sort) {
		return querydsl.applySorting(sort, query).fetch();
	}

	@Override
	public long update(T entity, C criteria) {
		JPAUpdateClause jpaUpdateClause = queryFactory.update(path);
		entity.mapForUpdate(jpaUpdateClause);
		return jpaUpdateClause.where(criteria.getFilter()).execute();
	}

	@Override
	public long deleteByCriteria(C criteria) {
		JPADeleteClause jpaDeleteClause = queryFactory.delete(path);
		return jpaDeleteClause.where(criteria.getFilter()).execute();
	}

	public static QueryHints getRelatedDataHints(String... hints) {
		if (ArrayUtils.isNotEmpty(hints)) {
			MutableQueryHints queryHints = new MutableQueryHints();
			queryHints.add("jakarta.persistence.fetchgraph", String.join(",", hints));
			return queryHints;
		}
		return null;
	}
}