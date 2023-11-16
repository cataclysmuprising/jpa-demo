package com.example.backend.config.security;

import com.example.persistence.dto.AuthenticatedClientDTO;
import com.example.persistence.service.AdministratorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static com.example.persistence.utils.LoggerConstants.*;

@Component
public class AuthenticationUserService implements UserDetailsService {
	private static final Logger applicationLogger = LogManager.getLogger("applicationLogs." + AuthenticationUserService.class.getName());
	private static final Logger errorLogger = LogManager.getLogger("errorLogs." + AuthenticationUserService.class.getName());

	@Autowired
	private AdministratorService administratorService;

	@Override
	public final UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		//applicationLogger.info(LOG_BREAKER_OPEN);
		try {
			AuthenticatedClientDTO client = administratorService.findAuthenticatedClient(loginId);
			if (client == null) {
				throw new UsernameNotFoundException("Login client doesn`t exist !");
			}
			applicationLogger.info(LOG_PREFIX + "Roles of :" + client.getName() + " are " + client.getRoles() + LOG_SUFFIX);
			// pass authUser object and roles to LoggedUser
			AuthenticatedClient loggedUser = new AuthenticatedClient(client, client.getRoles());
			applicationLogger.info(LOG_BREAKER_CLOSE);
			return loggedUser;
		}
		catch (Exception e) {
			e.printStackTrace();
			errorLogger.error(LOG_PREFIX + "Login client doesn`t exist." + LOG_SUFFIX, e);
			return null;
		}
	}
}
