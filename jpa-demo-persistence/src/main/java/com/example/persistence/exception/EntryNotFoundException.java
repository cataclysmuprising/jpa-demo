package com.example.persistence.exception;

import java.io.Serial;

public class EntryNotFoundException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 3902596794255918198L;
	private final Long id;

	public EntryNotFoundException(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
