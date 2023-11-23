package com.example.backend.validators;

import com.example.backend.common.response.PageMode;
import com.example.backend.common.validation.BaseValidator;
import com.example.backend.common.validation.FieldValidator;
import com.example.persistence.criteria.AdministratorCriteria;
import com.example.persistence.dto.AdministratorDTO;
import com.example.persistence.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AdminValidator extends BaseValidator {

	@Autowired
	private AdministratorService administratorService;

	@Override
	public boolean supports(Class clazz) {
		return AdministratorDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		AdministratorDTO adminDto = (AdministratorDTO) obj;
		//Name
		validateIsEmpty(new FieldValidator("name", "Name", adminDto.getName(), errors));
		validateIsValidMaxValue(new FieldValidator("name", "Name", adminDto.getName(), errors), 50);
		//Name
		validateIsEmpty(new FieldValidator("loginId", "Login ID", adminDto.getLoginId(), errors));
		validateIsValidMaxValue(new FieldValidator("loginId", "Login ID", adminDto.getLoginId(), errors), 50);
		if (pageMode == PageMode.CREATE) {
			if (errors.getFieldErrors("loginId").size() == 0) {

				AdministratorCriteria criteria = new AdministratorCriteria();
				criteria.setLoginId(adminDto.getLoginId());
				if (administratorService.exists(criteria)) {
					errors.rejectValue("loginId", "", messageSource.getMessage("Validation.common.Field.Unique", "Login ID", adminDto.getLoginId()));
				}
			}

			//Password
			validateIsEmpty(new FieldValidator("password", "Password", adminDto.getPassword(), errors));
			validateIsValidMinValue(new FieldValidator("password", "Password", adminDto.getPassword(), errors), 8);
			//Confirm Password
			validateIsEmpty(new FieldValidator("confirmPassword", "Confirm Password", adminDto.getConfirmPassword(), errors));
			validateIsValidMinValue(new FieldValidator("confirmPassword", "Confirm Password", adminDto.getConfirmPassword(), errors), 8);

			validateIsEqual("confirmPassword", new FieldValidator("password", "Password", adminDto.getPassword(), errors), new FieldValidator("confirmPassword", "Confirm Password", adminDto.getConfirmPassword(), errors), errors);
		}
	}
}
