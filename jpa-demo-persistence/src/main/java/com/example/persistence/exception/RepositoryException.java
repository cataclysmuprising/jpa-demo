package com.example.persistence.exception;

import java.io.Serial;

public class RepositoryException extends Exception {

	@Serial
	private static final long serialVersionUID = -7512756642706562435L;

	public RepositoryException() {
		super();
	}

	public RepositoryException(String message) {
		super(message);
	}

	public RepositoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepositoryException(Throwable cause) {
		super(cause);
	}
}
