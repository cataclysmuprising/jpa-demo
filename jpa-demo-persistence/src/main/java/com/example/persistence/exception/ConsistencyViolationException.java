package com.example.persistence.exception;

public class ConsistencyViolationException extends Exception {
	private static final long serialVersionUID = -4061374500120185334L;

	public ConsistencyViolationException() {
		super();
	}

	public ConsistencyViolationException(String message) {
		super(message);
	}

	public ConsistencyViolationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConsistencyViolationException(Throwable cause) {
		super(cause);
	}
}
