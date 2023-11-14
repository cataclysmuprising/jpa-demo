package com.example.persistence.repository.base;

import com.example.persistence.criteria.AbstractCriteria;
import com.example.persistence.entity.AbstractEntity;
import com.example.persistence.entity.QAbstractEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface AbstractRepository<T extends AbstractEntity, C extends AbstractCriteria<?>, ID> extends QuerydslPredicateExecutor<T> {

	List<Long> findIds(QAbstractEntity entity, Predicate predicate);

	List<Long> findIds(QAbstractEntity entity, Predicate predicate, Sort sort);

	Optional<T> findById(ID id);

	Optional<T> findOne(Predicate predicate, String... hints);

	List<T> findAll(Predicate predicate, String... hints);

	Page<T> findAll(Predicate predicate, Pageable pageable, String... hints);

	<S extends T> S save(S entity);

	<S extends T> S saveAndFlush(S entity);

	<S extends T> List<S> saveAll(Iterable<S> entities);

	long update(T entity, C criteria);

	void deleteById(ID id);

	long deleteByCriteria(C criteria);

	void flush();
}
