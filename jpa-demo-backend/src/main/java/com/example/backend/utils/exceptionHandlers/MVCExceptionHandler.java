package com.example.backend.utils.exceptionHandlers;

import com.example.backend.controller.mvc.BaseMVCController;
import com.example.persistence.exception.*;
import com.example.persistence.utils.LoggerConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;

@ControllerAdvice
public class MVCExceptionHandler {
	private static final Logger errorLogger = LogManager.getLogger("errorLogs." + MVCExceptionHandler.class.getName());

	@Autowired
	protected Environment environment;

	@ExceptionHandler({NoHandlerFoundException.class, ContentNotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Object handleNoHandlerFoundException(Exception e, Authentication auth, HttpServletRequest request) {
		logErrorMessage(e);
		if (request.getRequestURI().contains("/api/")) {
			ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
			problemDetail.setTitle(HttpStatus.NOT_FOUND.getReasonPhrase());
			problemDetail.setType(URI.create("https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.4"));
			problemDetail.setProperty("timestamp", Instant.now());
			return problemDetail;
		}
		else {
			HashMap<String, Object> attributes = new HashMap<>();
			attributes.put("referer", request.getHeader("referer"));
			return getErrorView("error/404", auth, attributes);
		}
	}

	@ExceptionHandler(HttpSessionRequiredException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ModelAndView handleSessionExpired(Authentication auth, HttpServletRequest request) {
		logErrorMessage(new Exception("Session was expired !"));
		HashMap<String, Object> attributes = new HashMap<>();
		attributes.put("referer", request.getHeader("referer"));
		return getErrorView("error/401", auth, attributes);
	}

	@ExceptionHandler(SecurityException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public Object handleUnAuthorizeAccess(Exception e, Authentication auth, HttpServletRequest request) {
		logErrorMessage(e);
		if (request.getRequestURI().contains("/api/")) {
			ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
			problemDetail.setTitle(HttpStatus.UNAUTHORIZED.getReasonPhrase());
			problemDetail.setType(URI.create("https://datatracker.ietf.org/doc/html/rfc7235#section-3.1"));
			problemDetail.setProperty("timestamp", Instant.now());
			return problemDetail;
		}
		else {
			return getErrorView("error/401", auth, null);
		}
	}

	@ExceptionHandler({AccessDeniedException.class, AuthenticationException.class})
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Object handleAuthenticationException(AuthenticationException e, Authentication auth, HttpServletRequest request) {
		logErrorMessage(e);
		if (request.getRequestURI().contains("/api/")) {
			ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
			problemDetail.setTitle(HttpStatus.FORBIDDEN.getReasonPhrase());
			problemDetail.setType(URI.create("https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.3"));
			problemDetail.setProperty("timestamp", Instant.now());
			return problemDetail;
		}
		else {
			return getErrorView("error/403", auth, null);
		}
	}

	@ExceptionHandler({DocumentExpiredException.class})
	@ResponseStatus(HttpStatus.GONE)
	public ModelAndView handleDocumentExpiredException(Exception e, Authentication auth, HttpServletRequest request) {
		logErrorMessage(e);
		return getErrorView("error/410", auth, null);
	}

	@ExceptionHandler({MissingServletRequestParameterException.class, UnsatisfiedServletRequestParameterException.class, ServletRequestBindingException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Object handleBadRequest(Exception e, Authentication auth, HttpServletRequest request) {
		logErrorMessage(e);
		if (request.getRequestURI().contains("/api/")) {
			ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
			problemDetail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
			problemDetail.setType(URI.create("https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.1"));
			problemDetail.setProperty("timestamp", Instant.now());
			return problemDetail;
		}
		else {
			return getErrorView("error/400", auth, null);
		}
	}

	@ExceptionHandler({MethodArgumentTypeMismatchException.class, MethodArgumentNotValidException.class, NoSuchMethodException.class, HttpRequestMethodNotSupportedException.class})
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public Object handleMethodNotAllowed(Exception e, Authentication auth, HttpServletRequest request) {
		logErrorMessage(e);
		if (request.getRequestURI().contains("/api/")) {
			ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
			problemDetail.setTitle(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
			problemDetail.setType(URI.create("https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.5"));
			problemDetail.setProperty("timestamp", Instant.now());
			return problemDetail;
		}
		else {
			return getErrorView("error/405", auth, null);
		}
	}

	@ExceptionHandler(DuplicatedEntryException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public Object handleDuplicatedEntryException(DuplicatedEntryException e, Authentication auth, HttpServletRequest request) {
		logErrorMessage(e);
		if (request.getRequestURI().contains("/api/")) {
			ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
			problemDetail.setTitle(HttpStatus.CONFLICT.getReasonPhrase());
			problemDetail.setType(URI.create("https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.8"));
			problemDetail.setProperty("timestamp", Instant.now());
			return problemDetail;
		}
		else {
			HashMap<String, Object> attributes = new HashMap<>();
			attributes.put("referer", request.getHeader("referer"));
			return getErrorView("error/409", auth, attributes);
		}
	}

	@ExceptionHandler(ConsistencyViolationException.class)
	@ResponseStatus(HttpStatus.IM_USED)
	public Object handleConsistencyViolationException(ConsistencyViolationException e, Authentication auth, HttpServletRequest request) {
		logErrorMessage(e);
		if (request.getRequestURI().contains("/api/")) {
			ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.IM_USED, e.getMessage());
			problemDetail.setTitle(HttpStatus.IM_USED.getReasonPhrase());
			problemDetail.setType(URI.create("https://datatracker.ietf.org/doc/html/rfc3229#section-10.4.1"));
			problemDetail.setProperty("timestamp", Instant.now());
			return problemDetail;
		}
		else {
			HashMap<String, Object> attributes = new HashMap<>();
			attributes.put("referer", request.getHeader("referer"));
			return getErrorView("error/226", auth, attributes);
		}
	}

	@ExceptionHandler({BusinessException.class, RuntimeException.class, Exception.class, Throwable.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Object handleServerError(Exception e, Authentication auth, HttpServletRequest request) {
		logErrorMessage(e);
		if (request.getRequestURI().contains("/api/")) {
			ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			problemDetail.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			problemDetail.setType(URI.create("https://datatracker.ietf.org/doc/html/rfc7231#section-6.6.1"));
			problemDetail.setProperty("errorCategory", "Generic");
			problemDetail.setProperty("timestamp", Instant.now());
			return problemDetail;
		}
		else {
			return getErrorView("error/500", auth, null);
		}
	}

	private void logErrorMessage(Exception e) {
		errorLogger.error(LoggerConstants.LOG_BREAKER_OPEN);
		errorLogger.error(e.getMessage(), e);
		errorLogger.error(LoggerConstants.LOG_BREAKER_CLOSE);
	}

	private ModelAndView getErrorView(String errorPage, Authentication auth, HashMap<String, Object> attributes) {
		ModelAndView modelAndView = new ModelAndView(errorPage);
		modelAndView.setViewName("fragments/layouts/error/template");
		modelAndView.addObject("view", errorPage);

		if (attributes != null) {
			attributes.forEach(modelAndView::addObject);
		}
		modelAndView.addObject("projectVersion", BaseMVCController.getProjectVersion());
		modelAndView.addObject("buildNumber", BaseMVCController.getBuildNumber());
		modelAndView.addObject("appShortName", BaseMVCController.getAppShortName());
		modelAndView.addObject("appFullName", BaseMVCController.getAppFullName());
		modelAndView.addObject("pageName", "Error !");
		modelAndView.addObject("isProduction", !"dev".equals(environment.getActiveProfiles()[0]));
		return modelAndView;
	}
}
