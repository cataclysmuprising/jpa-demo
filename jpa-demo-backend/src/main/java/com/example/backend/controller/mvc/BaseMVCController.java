package com.example.backend.controller.mvc;

import com.example.backend.BackendApplication;
import com.example.backend.config.security.AuthenticatedClient;
import com.example.persistence.dto.AuthenticatedClientDTO;
import com.example.persistence.exception.BusinessException;
import com.example.persistence.service.ActionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public abstract class BaseMVCController {

	private static String projectVersion;
	private static String buildNumber;
	@Autowired
	protected Environment environment;
	@Autowired
	protected MessageSource messageSource;
	@Autowired
	protected ObjectMapper mapper;
	@Autowired
	private ActionService actionService;
	@Autowired
	private ServletContext servletContext;

	public BaseMVCController() {
		projectVersion = "1.0";
		buildNumber = DateTimeFormatter.ofPattern("ddMMyyyy-HHmm").format(LocalDateTime.now());
	}

	public static String getProjectVersion() {
		return projectVersion;
	}

	public static String getBuildNumber() {
		return buildNumber;
	}

	public static String getAppShortName() {
		return "My Website";
	}

	public static String getAppFullName() {
		return "My Website Backend";
	}

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

	protected void setAuthorities(Model model, String pageName) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			model.addAttribute("pageName", pageName);
			HashMap<String, Boolean> accessments = new HashMap<>();
			AuthenticatedClient loginUser = getSignInClientInfo();
			AuthenticatedClientDTO client = loginUser.getUserDetail();
			List<String> actions = null;
			try {
				actions = actionService.selectAvailableActionsForUser(pageName, BackendApplication.APP_NAME, client.getRoleIds());
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
		model.addAttribute("contextPath", servletContext.getContextPath());
		model.addAttribute("projectVersion", getProjectVersion());
		model.addAttribute("buildNumber", getBuildNumber());
		model.addAttribute("appShortName", getAppShortName());
		model.addAttribute("appFullName", getAppFullName());
		model.addAttribute("isProduction", !"dev".equals(environment.getActiveProfiles()[0]));
	}

	public abstract void subInit(Model model);
}
