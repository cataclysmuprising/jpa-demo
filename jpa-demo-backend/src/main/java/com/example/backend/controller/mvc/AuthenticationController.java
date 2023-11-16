package com.example.backend.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Controller
public class AuthenticationController {

	@Autowired
	protected Environment environment;
	@Autowired
	private MessageSource messageSource;

	@GetMapping("/login")
	public String login(Model model, @RequestParam(required = false, name = "error") String error) {
		// set profile mode
		model.addAttribute("isProduction", !"dev".equals(environment.getActiveProfiles()[0]));

		Locale locale = Locale.ENGLISH;
		if (error != null) {
			model.addAttribute("messageStyle", "alert-danger");
			switch (error) {
				case "account-disabled":
					model.addAttribute("pageMessage", messageSource.getMessage("Serverity.common.auth.message.disabled", null, locale));
					break;
				case "account-locked":
					model.addAttribute("pageMessage", messageSource.getMessage("Serverity.common.auth.message.locked", null, locale));
					break;
				case "account-expired":
					model.addAttribute("pageMessage", messageSource.getMessage("Serverity.common.auth.message.expired", null, locale));
					break;
				default:
					model.addAttribute("pageMessage", messageSource.getMessage("Serverity.common.auth.message.badCredentials", null, locale));
					break;
			}
		}
		return "login";
	}
}
