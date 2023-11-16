package com.example.backend.config.security;

import com.example.backend.utils.SecurityUtil;
import com.example.persistence.dto.AuthenticatedClientDTO;
import com.example.persistence.dto.LoginHistoryDTO;
import com.example.persistence.service.LoginHistoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.example.persistence.utils.LoggerConstants.LOG_PREFIX;
import static com.example.persistence.utils.LoggerConstants.LOG_SUFFIX;

@Component
public class RememberMeAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private static final Logger applicationLogger = LogManager.getLogger("applicationLogs." + RememberMeAuthenticationSuccessHandler.class.getName());
	private static final Logger errorLogger = LogManager.getLogger("errorLogs." + RememberMeAuthenticationSuccessHandler.class.getName());

	@Autowired
	private LoginHistoryService loginHistoryService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		String loginId = authentication.getName();
		applicationLogger.info(LOG_PREFIX + "Login Client with Login ID '" + loginId + "' has successfully signed in." + LOG_SUFFIX);
		try {
			AuthenticatedClient authClient = (AuthenticatedClient) authentication.getPrincipal();
			AuthenticatedClientDTO client = authClient.getUserDetail();
			LoginHistoryDTO loginHistory = new LoginHistoryDTO();
			loginHistory.setIpAddress(SecurityUtil.getClientIp(request));
			loginHistory.setOs(SecurityUtil.getOperatingSystem(request));
			loginHistory.setClientAgent(SecurityUtil.getUserAgent(request));
			loginHistory.setLoginDate(LocalDateTime.now());
			loginHistory.setClientId(client.getId());
			loginHistory.setClientType(client.getClientType());
			loginHistoryService.create(loginHistory, client.getId());
			applicationLogger.info(LOG_PREFIX + "Recorded in loginHistory for Login ID '" + loginId + "'." + LOG_SUFFIX);
		}
		catch (Exception e) {
			e.printStackTrace();
			errorLogger.error(LOG_PREFIX + "Can't save in loginHistory for Login ID '" + loginId + "'" + LOG_SUFFIX, e);
		}

		super.setAlwaysUseDefaultTargetUrl(true);
		super.setDefaultTargetUrl(request.getRequestURL().toString());
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
