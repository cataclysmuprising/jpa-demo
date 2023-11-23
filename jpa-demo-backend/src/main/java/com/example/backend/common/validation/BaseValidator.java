package com.example.backend.common.validation;

import com.example.backend.common.converters.LocalizedMessageResolver;
import com.example.backend.common.exception.UnSupportedValidationCheckException;
import com.example.backend.common.response.PageMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BaseValidator implements Validator {

	protected PageMode pageMode;

	protected LocalizedMessageResolver messageSource;

	@Autowired
	public void setMessageSource(LocalizedMessageResolver messageSource) {
		this.messageSource = messageSource;
	}

	public boolean validateIsEmpty(FieldValidator fieldValidator) {
		String targetId = fieldValidator.getTargetId();
		Errors errors = fieldValidator.getErrors();
		// no more checking needed
		List<FieldError> fieldErrors = errors.getFieldErrors(targetId);
		if (!fieldErrors.isEmpty()) {
			return true;
		}
		String displayName = fieldValidator.getDisplayName();
		Object target = fieldValidator.getTarget();
		if (target == null) {
			errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.Required", displayName));
			return true;
		}
		else if (target instanceof String) {
			if (((String) target).isEmpty()) {
				errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.Required", displayName));
				return true;
			}
		}
		else if (target instanceof Collection) {
			if (((Collection<?>) target).isEmpty()) {
				errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.ChooseOne", displayName));
				return true;
			}
		}
		else if (target instanceof Map) {
			if (((Map<?, ?>) target).isEmpty()) {
				errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.ChooseOne", displayName));
				return true;
			}
		}
		else if (target.getClass().isArray()) {
			if (((Object[]) target).length == 0) {
				errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.ChooseOne", displayName));
				return true;
			}
		}
		return false;
	}

	public boolean validateIsEmpty(Object target) {
		if (target == null) {
			return true;
		}
		else if (target instanceof String) {
			return ((String) target).isEmpty();
		}
		else if (target instanceof Collection) {
			return ((Collection<?>) target).isEmpty();
		}
		else if (target instanceof Map) {
			return ((Map<?, ?>) target).isEmpty();
		}
		else if (target.getClass().isArray()) {
			return ((Object[]) target).length == 0;
		}
		return false;
	}

	public void validateIsEqual(String targetId, FieldValidator fieldValidator1, FieldValidator fieldValidator2, Errors errors) {
		if (!validateIsEmpty(fieldValidator1) && !validateIsEmpty(fieldValidator2)) {
			if (!fieldValidator1.getTarget().equals(fieldValidator2.getTarget())) {
				errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.DoNotMatch", fieldValidator1.getDisplayName(), fieldValidator2.getDisplayName()));
			}
		}
	}

	public void validateIsValidMinValue(FieldValidator fieldValidator, Number number) {
		if (!validateIsEmpty(fieldValidator)) {
			String targetId = fieldValidator.getTargetId();
			String displayName = fieldValidator.getDisplayName();
			Object target = fieldValidator.getTarget();
			Errors errors = fieldValidator.getErrors();

			if (target instanceof String) {
				if (target.toString().length() < number.intValue()) {
					errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.Min.String", displayName, number));
				}
			}
			else if (target instanceof Number inputNumber) {
				if (inputNumber.doubleValue() < number.doubleValue()) {
					errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.Min.Number", displayName, number));
				}
			}
		}
	}

	public void validateIsValidMaxValue(FieldValidator fieldValidator, Number number) {
		if (!validateIsEmpty(fieldValidator)) {
			String targetId = fieldValidator.getTargetId();
			String displayName = fieldValidator.getDisplayName();
			Object target = fieldValidator.getTarget();
			Errors errors = fieldValidator.getErrors();

			if (target instanceof String) {
				if (target.toString().length() > number.intValue()) {
					errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.Max.String", displayName, number));
				}
			}
			else if (target instanceof Number inputNumber) {
				if (inputNumber.doubleValue() > number.doubleValue()) {
					errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.Max.Number", displayName, number));
				}
			}
			else {
				throw new UnSupportedValidationCheckException();
			}
		}
	}

	public void validateIsValidRangeValue(FieldValidator fieldValidator, Number min, Number max) {
		if (!validateIsEmpty(fieldValidator)) {
			String targetId = fieldValidator.getTargetId();
			String displayName = fieldValidator.getDisplayName();
			Object target = fieldValidator.getTarget();
			Errors errors = fieldValidator.getErrors();
			if (target instanceof String) {
				if (target.toString().length() < min.intValue() || target.toString().length() > max.intValue()) {
					errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.Range.String", displayName, min, max));
				}
			}
			else if (target instanceof Number number) {
				if (number.doubleValue() < min.doubleValue() || number.doubleValue() > max.doubleValue()) {
					errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.Range.Number", displayName, min, max));
				}
			}
			else {
				throw new UnSupportedValidationCheckException();
			}
		}
	}

	public void validateIsValidDigits(FieldValidator fieldValidator) {
		if (!validateIsEmpty(fieldValidator)) {
			String targetId = fieldValidator.getTargetId();
			String displayName = fieldValidator.getDisplayName();
			if (fieldValidator.getTarget() instanceof String target) {
				Errors errors = fieldValidator.getErrors();
				if (!Pattern.matches("[\\-\\+]?\\d+", target)) {
					errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.InvalidNumber", displayName));
				}
			}
			else {
				throw new UnSupportedValidationCheckException();
			}
		}
	}

	// http://stackoverflow.com/questions/15111420/how-to-check-if-a-string-contains-only-digits-in-java#15111450
	public void validateIsValidUnSignDigits(FieldValidator fieldValidator) {
		if (!validateIsEmpty(fieldValidator)) {
			String targetId = fieldValidator.getTargetId();
			String displayName = fieldValidator.getDisplayName();
			if (fieldValidator.getTarget() instanceof String target) {
				Errors errors = fieldValidator.getErrors();
				if (!Pattern.matches("\\d+", target)) {
					errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.InvalidUnsignNumber", displayName));
				}
			}
			else {
				throw new UnSupportedValidationCheckException();
			}
		}
	}

	public void validateIsValidAlphaBatics(FieldValidator fieldValidator) {
		if (!validateIsEmpty(fieldValidator)) {
			String targetId = fieldValidator.getTargetId();
			String displayName = fieldValidator.getDisplayName();
			if (fieldValidator.getTarget() instanceof String target) {
				Errors errors = fieldValidator.getErrors();
				if (!Pattern.matches("^[a-zA-Z_ ]+$", target)) {
					errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.InvalidAlphabet", displayName));
				}
			}
			else {
				throw new UnSupportedValidationCheckException();
			}
		}
	}

	// include space and underscore
	public void validateIsValidAlphaNumerics(FieldValidator fieldValidator) {
		if (!validateIsEmpty(fieldValidator)) {
			String targetId = fieldValidator.getTargetId();
			String displayName = fieldValidator.getDisplayName();
			if (fieldValidator.getTarget() instanceof String target) {
				Errors errors = fieldValidator.getErrors();
				if (!Pattern.matches("^[a-zA-Z_0-9 ]+$", target)) {
					errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.InvalidAlphaNumeric", displayName));
				}
			}
			else {
				throw new UnSupportedValidationCheckException();
			}
		}
	}

	// to validate for sql queries
	public void validateIsValidQueryString(FieldValidator fieldValidator) {
		if (!validateIsEmpty(fieldValidator)) {
			String targetId = fieldValidator.getTargetId();
			String displayName = fieldValidator.getDisplayName();
			if (fieldValidator.getTarget() instanceof String target) {
				Errors errors = fieldValidator.getErrors();
				if (!Pattern.matches("^[a-zA-Z_0-9 \\/\\-\\.\\@]+$", target)) {
					errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.InvalidQueryString", displayName));
				}
			}
			else {
				throw new UnSupportedValidationCheckException();
			}
		}
	}

	public void validateIsValidCapitalLetters(FieldValidator fieldValidator) {
		if (!validateIsEmpty(fieldValidator)) {
			String targetId = fieldValidator.getTargetId();
			String displayName = fieldValidator.getDisplayName();
			if (fieldValidator.getTarget() instanceof String target) {
				Errors errors = fieldValidator.getErrors();
				if (!Pattern.matches("^[A-Z_ \\-]+$", target)) {
					errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.InvalidCapitalLetter", displayName));
				}
			}
			else {
				throw new UnSupportedValidationCheckException();
			}
		}
	}

	public void validateIsValidSmallLetters(FieldValidator fieldValidator) {
		if (!validateIsEmpty(fieldValidator)) {
			String targetId = fieldValidator.getTargetId();
			String displayName = fieldValidator.getDisplayName();
			if (fieldValidator.getTarget() instanceof String target) {
				Errors errors = fieldValidator.getErrors();
				if (!Pattern.matches("^[a-z_ \\-]+$", target)) {
					errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.InvalidSmallLetter", displayName));
				}
			}
			else {
				throw new UnSupportedValidationCheckException();
			}
		}
	}

	// http://stackoverflow.com/questions/15805555/java-regex-to-validate-full-name-allow-only-spaces-and-letters#15806080
	public void validateIsValidUnicodeCharacters(FieldValidator fieldValidator) {
		if (!validateIsEmpty(fieldValidator)) {
			String targetId = fieldValidator.getTargetId();
			String displayName = fieldValidator.getDisplayName();
			if (fieldValidator.getTarget() instanceof String target) {
				Errors errors = fieldValidator.getErrors();
				if (!Pattern.matches("^[\\p{L} .'-]+$", target)) {
					errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.InvalidUnicode", displayName));
				}
			}
			else {
				throw new UnSupportedValidationCheckException();
			}
		}
	}

	// http://stackoverflow.com/questions/27938415/regex-for-password-atleast-1-letter-1-number-1-special-character-and-should#27938511
	// and add allowed space for between words
	public void validateIsValidPasswordPattern(FieldValidator fieldValidator) {
		if (!validateIsEmpty(fieldValidator)) {
			String targetId = fieldValidator.getTargetId();
			String displayName = fieldValidator.getDisplayName();
			if (fieldValidator.getTarget() instanceof String target) {
				Errors errors = fieldValidator.getErrors();
				if (!Pattern.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d][A-Za-z \\d!@#$%^&*()_+]{8,}$", target)) {
					errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.InvalidPassword", displayName));
				}
			}
			else {
				throw new UnSupportedValidationCheckException();
			}
		}
	}

	public boolean validateIsValidPasswordPattern(String target) {
		if (validateIsEmpty(target)) {
			return false;
		}
		return Pattern.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d][A-Za-z \\d!@#$%^&*()_+]{8,}$", target);
	}

	// http://stackoverflow.com/questions/8204680/java-regex-email/13013056#13013056
	public void validateIsValidEmail(FieldValidator fieldValidator) {
		if (!validateIsEmpty(fieldValidator)) {
			String targetId = fieldValidator.getTargetId();
			if (fieldValidator.getTarget() instanceof String target) {
				Errors errors = fieldValidator.getErrors();
				String emailPattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
				Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
				if (!pattern.matcher(target).find()) {
					errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.InvalidEmail", target));
				}
			}
			else {
				throw new UnSupportedValidationCheckException();
			}
		}
	}

	public boolean validateIsValidEmail(String target) {
		if (!validateIsEmpty(target)) {
			String emailPattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
			Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
			return pattern.matcher(target).find();
		}
		return false;
	}

	// http://stackoverflow.com/questions/163360/regular-expression-to-match-urls-in-java#163410
	public void validateIsValidURL(FieldValidator fieldValidator) {
		if (!validateIsEmpty(fieldValidator)) {
			String targetId = fieldValidator.getTargetId();
			if (fieldValidator.getTarget() instanceof String target) {
				try {
					new URI(target).toURL();
				}
				catch (MalformedURLException | URISyntaxException e) {
					Errors errors = fieldValidator.getErrors();
					errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.InvalidURL", target));
				}
			}
			else {
				throw new UnSupportedValidationCheckException();
			}
		}
	}

	// http://stackoverflow.com/questions/308122/simple-regular-expression-for-a-decimal-with-a-precision-of-2#308216
	public void validateIsValidFloatingPointNumbers(FieldValidator fieldValidator, int precision) {
		if (!validateIsEmpty(fieldValidator)) {
			String targetId = fieldValidator.getTargetId();
			String displayName = fieldValidator.getDisplayName();
			Errors errors = fieldValidator.getErrors();
			if (fieldValidator.getTarget() instanceof Double) {
				Double target = (Double) fieldValidator.getTarget();
				if (!Pattern.matches("[\\-\\+]?[0-9]+(\\.[0-9]{1," + precision + "})?$", target.toString())) {
					errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.InvalidFloatingPointNumber", displayName));
				}
			}
			else if (fieldValidator.getTarget() instanceof Float) {
				if (fieldValidator.getTarget() instanceof Double) {
					Float target = (Float) fieldValidator.getTarget();
					if (!Pattern.matches("[\\-\\+]?[0-9]+(\\.[0-9]{1," + precision + "})?$", target.toString())) {
						errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.InvalidFloatingPointNumber", displayName, precision));
					}
				}
			}
			else {
				throw new UnSupportedValidationCheckException();
			}
		}
	}

	public boolean validatePattern(FieldValidator fieldValidator, String pattern) {

		String targetId = fieldValidator.getTargetId();
		Errors errors = fieldValidator.getErrors();
		String displayName = fieldValidator.getDisplayName();

		if (!validateIsEmpty(fieldValidator)) {
			if (!Pattern.compile(pattern).matcher(String.valueOf(fieldValidator.getTarget())).matches()) {
				errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.InvalidFormat", displayName));
				return false;
			}
		}

		return true;
	}

	public PageMode getPageMode() {
		return pageMode;
	}

	public void setPageMode(PageMode pageMode) {
		this.pageMode = pageMode;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public void validate(Object target, Errors errors) {
	}
}
