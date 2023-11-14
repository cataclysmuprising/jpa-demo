package com.example.persistence.exception;

public class EntryNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -4879046416449477092L;
	private final Long id;

	public EntryNotFoundException(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
