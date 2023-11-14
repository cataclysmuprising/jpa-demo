package com.example.persistence.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.validation.ConstraintViolation;
import java.util.Set;

public class ValidationFailedException extends RuntimeException {
	private static final long serialVersionUID = 4962362038607877412L;
	private static final Logger errorLogger = LogManager.getLogger("errorLogs." + ValidationFailedException.class);

	public ValidationFailedException(String message) {
		super(message);
	}

	public ValidationFailedException() {
		super();
	}

	public ValidationFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidationFailedException(Throwable cause) {
		super(cause);
	}

	public <E> ValidationFailedException(Set<ConstraintViolation<E>> constraintViolations) {
		super();
		errorLogger.error("====================== Validation Error ! ======================");
		constraintViolations.forEach(violation -> {
			errorLogger.error(violation.getPropertyPath().toString() + " [" + violation.getInvalidValue() + "] ==> " + violation.getMessage());
		});
		errorLogger.error("================================================================");
	}
}