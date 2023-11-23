package com.example.backend.common.aspect;

import com.example.backend.common.annotation.ValidateEntity;
import com.example.backend.common.exception.ValidationFailedException;
import com.example.backend.common.response.PageMessage;
import com.example.backend.common.response.PageMessageStyle;
import com.example.backend.common.response.PageMode;
import com.example.backend.common.validation.BaseValidator;
import com.example.persistence.dto.AbstractDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.support.BindingAwareModelMap;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
@Aspect
@Order(1)
public class CheckValidationAspect extends BaseAspect {
	private static final Logger errorLogger = LogManager.getLogger("errorLogs." + CheckValidationAspect.class.getName());
	private ApplicationContext appContext;

	private MessageSource messageSource;

	@Autowired
	public void setAppContext(ApplicationContext appContext) {
		this.appContext = appContext;
	}

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Before(value = "methodAnnotatedWithValidateEntity(validateEntity) && publicMethod()", argNames = "joinPoint,validateEntity")
	public void handleExeptionforServletMethods(JoinPoint joinPoint, ValidateEntity validateEntity) {
		Locale locale = LocaleContextHolder.getLocale();
		String errorView = validateEntity.errorView();
		PageMode pageMode = validateEntity.pageMode();
		Model model = null;
		BeanPropertyBindingResult beanBindingResults = null;
		Object validationTarget = null;
		Object[] arguments = joinPoint.getArgs();
		for (Object arg : arguments) {
			if (arg != null) {
				if (arg instanceof BindingAwareModelMap) {
					model = (Model) arg;
				}
				if (arg instanceof BeanPropertyBindingResult) {
					beanBindingResults = (BeanPropertyBindingResult) arg;
				}
				if (arg instanceof AbstractDTO) {
					validationTarget = arg;
				}
			}
		}
		BaseValidator validator = appContext.getBean(validateEntity.validator());
		validator.setPageMode(pageMode);
		assert validationTarget != null;
		assert beanBindingResults != null;
		validator.validate(validationTarget, beanBindingResults);
		if (beanBindingResults.hasErrors()) {
			errorLogger.error(LOG_PREFIX + "Validation failed for '" + validationTarget.getClass().getSimpleName() + "'." + LOG_SUFFIX);
			errorLogger.error(LOG_BREAKER_OPEN);
			if (errorLogger.isDebugEnabled()) {
				Map<String, String> validationErrors = new HashMap<>();
				List<FieldError> errorFields = beanBindingResults.getFieldErrors();
				errorFields.forEach(item -> {
					if (!validationErrors.containsKey(item.getField())) {
						validationErrors.put(item.getField(), item.getDefaultMessage());
					}
				});
				validationErrors.forEach((key, value) -> errorLogger.debug(key + " ==> " + value));
			}
			errorLogger.error(LOG_BREAKER_CLOSE);
			if (model != null) {
				model.addAttribute("pageMode", pageMode);
			}
			if (model != null) {
				Map<String, String> validationErrors = new HashMap<>();
				List<FieldError> errorFields = beanBindingResults.getFieldErrors();
				errorFields.forEach(item -> {
					if (!validationErrors.containsKey(item.getField())) {
						validationErrors.put(item.getField(), item.getDefaultMessage());
					}
				});
				model.addAttribute("validationErrors", validationErrors);
				model.addAttribute("pageMessage", new PageMessage("Validation Error", messageSource.getMessage("Validation.common.Page.ValidationErrorMessage", null, locale), PageMessageStyle.ERROR.getValue()));
			}
			throw new ValidationFailedException(model, errorView);
		}
	}

	@Pointcut("@annotation(validateEntity)")
	public void methodAnnotatedWithValidateEntity(ValidateEntity validateEntity) {
	}
}
