package com.example.persistence.exception;

import java.io.Serial;

public class DuplicatedEntryException extends Exception {

	@Serial
	private static final long serialVersionUID = -7810429097570167171L;

	public DuplicatedEntryException() {
		super();
	}

	public DuplicatedEntryException(String message) {
		super(message);
	}

	public DuplicatedEntryException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicatedEntryException(Throwable cause) {
		super(cause);
	}
}
