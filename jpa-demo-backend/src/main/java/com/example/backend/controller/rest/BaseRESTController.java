package com.example.backend.controller.rest;

import com.example.backend.config.security.AuthenticatedClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

public abstract class BaseRESTController {

	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Autowired
	private ObjectMapper mapper;

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
}

