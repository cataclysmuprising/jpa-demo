package com.example.backend.config.security;

import com.example.backend.BackendApplication;
import com.example.persistence.service.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RoleBasedAccessDecisionManager implements AuthorizationManager<RequestAuthorizationContext> {
	private static final Logger applicationLogger = LogManager.getLogger("applicationLogs." + RoleBasedAccessDecisionManager.class.getName());
	private final RoleService roleService;

	@Override
	public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
		AuthorizationManager.super.verify(authentication, object);
	}

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
		AuthorizationDecision decision = new AuthorizationDecision(true);
		Authentication auth = authentication.get();
		applicationLogger.debug("Authentication : " + auth);
		if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
			String requestURL = context.getRequest().getServletPath();
			applicationLogger.debug("Filtering process executed by RoleBasedAccessDecisionManager for requested URL ==> {}", requestURL);
			List<String> urlAssociatedRoles = getAssociatedRolesByUrl(requestURL);
			if (urlAssociatedRoles == null || urlAssociatedRoles.isEmpty()) {
				applicationLogger.debug("Access restrictions were not defined for URL ==> {}.", requestURL);
				return null;
			}
			applicationLogger.debug("URL ==> {} was requested for Roles => {} ", requestURL, urlAssociatedRoles);
			List<String> authorities = auth.getAuthorities().parallelStream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
			applicationLogger.debug("Current Authenticated User has owned ==> {}", authorities);
			boolean hasAuthority = CollectionUtils.containsAny(urlAssociatedRoles, authorities);
			if (hasAuthority) {
				applicationLogger.debug("Access Granted : Filtered by RoleBasedAccessDecisionManager.");
				return new AuthorizationDecision(true);
			}
			else {
				applicationLogger.debug("Access Denied : Filtered by RoleBasedAccessDecisionManager.");
				return new AuthorizationDecision(false);
			}
		}
		applicationLogger.debug("Invalid authentication : need to reauthenticate for current user.");
		return new AuthorizationDecision(false);
	}

	public RoleBasedAccessDecisionManager(RoleService roleService) {
		this.roleService = roleService;
	}

	private List<String> getAssociatedRolesByUrl(String url) {
		List<String> urlAssociatedRoles = null;
		try {
			urlAssociatedRoles = roleService.selectRolesByActionURL(url, BackendApplication.APP_NAME);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return urlAssociatedRoles;
	}
}
