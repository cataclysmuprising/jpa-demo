package com.example.backend.utils.exceptionHandlers;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
public class ApiError {

	private HttpStatus status;
	private int statusCode;
	private String message;
	private List<String> errors;
	private Class<? extends Exception> errorClass;

	public ApiError() {
		super();
	}

	public ApiError(HttpStatus status, String message, List<String> errors, Class<? extends Exception> errorClass) {
		super();
		this.status = status;
		statusCode = status.value();
		this.message = message;
		this.errors = errors;
		this.errorClass = errorClass;
	}

	public ApiError(HttpStatus status, String message, String error, Class<? extends Exception> errorClass) {
		super();
		this.status = status;
		statusCode = status.value();
		this.message = message;
		this.errorClass = errorClass;
		errors = Collections.singletonList(error);
	}
}