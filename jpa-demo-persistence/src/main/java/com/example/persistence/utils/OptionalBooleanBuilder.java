package com.example.persistence.utils;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.function.Function;

// http://dyszkiewicz.me/programming/java/2016/05/14/handling-optional-values-using-querydsl-and-java8.html
public class OptionalBooleanBuilder {
	private final BooleanExpression predicate;

	public OptionalBooleanBuilder(BooleanExpression predicate) {
		this.predicate = predicate;
	}

	public <T> OptionalBooleanBuilder notNullAnd(Function<T, BooleanExpression> expressionFunction, T value) {
		if (value != null) {
			return new OptionalBooleanBuilder(predicate.and(expressionFunction.apply(value)));
		}
		return this;
	}

	public OptionalBooleanBuilder notEmptyAnd(Function<String, BooleanExpression> expressionFunction, String value) {
		if (!StringUtils.isEmpty(value)) {
			return new OptionalBooleanBuilder(predicate.and(expressionFunction.apply(value)));
		}
		return this;
	}

	public <T> OptionalBooleanBuilder notEmptyAnd(Function<Collection<T>, BooleanExpression> expressionFunction, Collection<T> collection) {
		if (!CollectionUtils.isEmpty(collection)) {
			return new OptionalBooleanBuilder(predicate.and(expressionFunction.apply(collection)));
		}
		return this;
	}

	public BooleanExpression build() {
		return predicate;
	}
}
