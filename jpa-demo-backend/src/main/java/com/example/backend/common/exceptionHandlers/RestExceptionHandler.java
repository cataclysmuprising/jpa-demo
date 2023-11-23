package com.example.backend.common.exceptionHandlers;

import com.example.persistence.exception.BusinessException;
import com.example.persistence.exception.ConsistencyViolationException;
import com.example.persistence.exception.ContentNotFoundException;
import com.example.persistence.exception.DuplicatedEntryException;
import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice(basePackages = {"com.example.backend.controller.rest"})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger errorLogger = LogManager.getLogger("errorLogs." + RestExceptionHandler.class.getName());

	@ExceptionHandler({ConstraintViolationException.class})
	public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		errorLogger.error(ex.getMessage(), ex);
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
		problemDetail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
		problemDetail.setType(URI.create("https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.1"));
		problemDetail.setProperty("timestamp", Instant.now());
		problemDetail.setProperty("violations", ex.getConstraintViolations());
		return problemDetail;
	}

	@ExceptionHandler({UnsatisfiedServletRequestParameterException.class})
	public ProblemDetail handleConstraintViolation(Exception ex, WebRequest request) {
		errorLogger.error(ex.getMessage(), ex);
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
		problemDetail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
		problemDetail.setType(URI.create("https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.1"));
		problemDetail.setProperty("timestamp", Instant.now());
		return problemDetail;
	}

	@ExceptionHandler({DuplicatedEntryException.class})
	public ProblemDetail handleDuplicatedEntryViolation(DuplicatedEntryException ex, WebRequest request) {
		errorLogger.error(ex.getMessage(), ex);
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
		problemDetail.setTitle(HttpStatus.CONFLICT.getReasonPhrase());
		problemDetail.setType(URI.create("https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.8"));
		problemDetail.setProperty("timestamp", Instant.now());
		return problemDetail;
	}

	@ExceptionHandler({ConsistencyViolationException.class})
	public ProblemDetail handleConsistencyViolation(ConsistencyViolationException ex, WebRequest request) {
		errorLogger.error(ex.getMessage(), ex);
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.IM_USED, ex.getMessage());
		problemDetail.setTitle(HttpStatus.IM_USED.getReasonPhrase());
		problemDetail.setType(URI.create("https://datatracker.ietf.org/doc/html/rfc3229#section-10.4.1"));
		problemDetail.setProperty("timestamp", Instant.now());
		return problemDetail;
	}

	// 401
	@ExceptionHandler({SecurityException.class})
	public ProblemDetail handleSecurityException(SecurityException ex, WebRequest request) {
		errorLogger.error(ex.getMessage(), ex);
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
		problemDetail.setTitle(HttpStatus.UNAUTHORIZED.getReasonPhrase());
		problemDetail.setType(URI.create("https://datatracker.ietf.org/doc/html/rfc7235#section-3.1"));
		problemDetail.setProperty("timestamp", Instant.now());
		return problemDetail;
	}

	// 403
	@ExceptionHandler({AccessDeniedException.class, AuthenticationException.class})
	public ProblemDetail handleAccessDeniedException(SecurityException ex, WebRequest request) {
		errorLogger.error(ex.getMessage(), ex);
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
		problemDetail.setTitle(HttpStatus.FORBIDDEN.getReasonPhrase());
		problemDetail.setType(URI.create("https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.3"));
		problemDetail.setProperty("timestamp", Instant.now());
		return problemDetail;
	}

	// 404
	@ExceptionHandler({ContentNotFoundException.class})
	public ProblemDetail handleContentNotFoundException(Exception ex, WebRequest request) {
		errorLogger.error(ex.getMessage(), ex);
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		problemDetail.setTitle(HttpStatus.NOT_FOUND.getReasonPhrase());
		problemDetail.setType(URI.create("https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.4"));
		problemDetail.setProperty("timestamp", Instant.now());
		return problemDetail;
	}

	// 500
	@ExceptionHandler({BusinessException.class, RuntimeException.class, Exception.class, Throwable.class})
	public ProblemDetail handleAll(Exception ex, WebRequest request) {
		errorLogger.error(ex.getMessage(), ex);
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		problemDetail.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		problemDetail.setType(URI.create("https://datatracker.ietf.org/doc/html/rfc7231#section-6.6.1"));
		problemDetail.setProperty("errorCategory", "Generic");
		problemDetail.setProperty("timestamp", Instant.now());
		return problemDetail;
	}
}
