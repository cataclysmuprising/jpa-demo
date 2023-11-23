package com.example.backend.common.validation;

import org.springframework.validation.Errors;

public class FieldValidator {
	private String targetId;
	private String displayName;
	private Object target;
	private Errors errors;

	public FieldValidator(String targetId, String displayName, Object target, Errors errors) {
		this.targetId = targetId;
		this.displayName = displayName;
		this.target = target;
		this.errors = errors;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Errors getErrors() {
		return errors;
	}

	public void setErrors(Errors errors) {
		this.errors = errors;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
}
