package com.example.backend.controller.mvc;

import com.example.backend.BackendApplication;
import com.example.backend.config.security.AuthenticatedClient;
import com.example.persistence.dto.AuthenticatedClientDTO;
import com.example.persistence.exception.BusinessException;
import com.example.persistence.service.ActionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.List;

public abstract class BaseMVCController {

	@Autowired
	protected Environment environment;

	@Autowired
	protected MessageSource messageSource;

	@Autowired
	protected ObjectMapper mapper;

	@Autowired
	private ActionService actionService;

	@ModelAttribute
	public void init(Model model) {
		AuthenticatedClient loginUser = getSignInClientInfo();
		if (loginUser != null) {
			AuthenticatedClientDTO client = loginUser.getUserDetail();
			model.addAttribute("loginUserName", client.getName());
			model.addAttribute("loginUserId", client.getId());
			model.addAttribute("contentId", client.getContentId());
		}
		setMetaData(model);
		subInit(model);
	}

	protected void setAuthorities(Model model, String page) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			model.addAttribute("page", page.toLowerCase());
			HashMap<String, Boolean> accessments = new HashMap<>();
			// capitalize start character
			if (!Character.isUpperCase(page.charAt(0))) {
				page = page.substring(0, 1).toUpperCase() + page.substring(1);
			}
			AuthenticatedClient loginUser = getSignInClientInfo();
			AuthenticatedClientDTO client = loginUser.getUserDetail();
			List<String> actions = null;
			try {
				actions = actionService.selectAvailableActionsForUser(page, BackendApplication.APP_NAME, client.getRoleIds());
			}
			catch (BusinessException e) {
				e.printStackTrace();
			}
			if (actions != null) {
				actions.forEach(actionName -> {
					model.addAttribute(actionName, true);
					accessments.put(actionName, true);
				});
				model.addAttribute("accessments", accessments);
			}
		}
	}

	protected String getApplicationContextPath(HttpServletRequest request) {
		return request.getRequestURL().toString().replace(request.getServletPath(), "");
	}

	protected Long getSignInClientId() {
		AuthenticatedClient loginUser = getSignInClientInfo();
		if (loginUser != null) {
			return loginUser.getId();
		}
		return null;
	}

	protected AuthenticatedClient getSignInClientInfo() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && !auth.getPrincipal().equals("anonymousUser")) {
			return mapper.convertValue(auth.getPrincipal(), AuthenticatedClient.class);
		}
		return null;
	}

	private void setMetaData(Model model) {
		// set profile mode
		model.addAttribute("isProduction", !"dev".equals(environment.getActiveProfiles()[0]));
	}

	public abstract void subInit(Model model);
}
