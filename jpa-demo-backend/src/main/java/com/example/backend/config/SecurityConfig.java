package com.example.backend.config;

import com.example.backend.config.security.*;
import com.example.persistence.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

	private static final String REMEMBER_ME_COOKIE = "jpademo_backend_rbm";

	@Autowired
	private UrlRequestAuthenticationSuccessHandler urlRequestsuccessHandler;

	@Autowired
	private RememberMeAuthenticationSuccessHandler rememberMeSuccessHandler;

	@Autowired
	private CustomAuthenticationFailureHandler failureHandler;

	@Autowired
	private RoleService roleService;

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationProvider authenticationProvider(AuthenticationUserService userDetailsService, PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder);
		return authProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	//@formatter:off
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationUserService userDetailsService) throws Exception {
		http
				.authorizeHttpRequests((authorize) ->
						authorize.requestMatchers(
								 "/login*"
								,"/accessDenied"
								,"/error/*"
								,"/static/**"
								,"/pub/**").permitAll()

								.requestMatchers("/sec/**")
								.access(new RoleBasedAccessDecisionManager(roleService))

								.anyRequest().authenticated()
				)
				.formLogin(
						form -> form
								.loginProcessingUrl("/login")
								.loginPage("/login")
								.usernameParameter("loginId")
								.passwordParameter("password")
								.failureHandler(failureHandler)
								.successHandler(urlRequestsuccessHandler)
								.permitAll()
				)
				.exceptionHandling(
						exceptionHandler -> exceptionHandler
								.accessDeniedPage("/accessDenied")
				)
				.sessionManagement(
						session -> session
								.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
								.sessionFixation().migrateSession()
				)
				.rememberMe(
						remember -> remember
								.key("cB11c2VyIiwic2NvcGUiOlsiYmFja2VuZCIsInJlY5QiLCJ3cmG0ZSIsInVwZG") // hash-key
								.rememberMeCookieName(REMEMBER_ME_COOKIE)
								.tokenValiditySeconds(4*604800) // 1 week , let the default 4 week
								.rememberMeParameter("remember-me")
								.userDetailsService(userDetailsService)
								.authenticationSuccessHandler(rememberMeSuccessHandler)
				)
				.logout(
						logout -> logout
								.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
								.deleteCookies(REMEMBER_ME_COOKIE)
								.clearAuthentication(true)
								.invalidateHttpSession(true)
								.logoutSuccessUrl("/login")
				);
		return http.build();
	}
	//@formatter:on

	@Autowired
	public void configureAuthentication(AuthenticationUserService userDetailsService, AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
		//@formatter:off
		auth
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder);
		//@formatter:on
	}
}
