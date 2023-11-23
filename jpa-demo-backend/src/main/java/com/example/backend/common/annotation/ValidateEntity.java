package com.example.backend.common.annotation;

import com.example.backend.common.response.PageMode;
import com.example.backend.common.validation.BaseValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface ValidateEntity {
	Class<? extends BaseValidator> validator();

	String errorView() default "";

	PageMode pageMode() default PageMode.VIEW;
}
