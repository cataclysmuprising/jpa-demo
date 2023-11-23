package com.example.backend.common.exceptionHandlers;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;

// To handle all exceptions out of Spring Controllers
@Component
public class ErrorHandlerFilter implements Filter {
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException {
		HttpServletRequest httpServletRequest = ((HttpServletRequest) request);

		HttpServletResponse httpServletResponse = ((HttpServletResponse) response);
		try {
			filterChain.doFilter(request, response);
		}
		catch (Exception e) {
			if (!response.isCommitted()) {
				redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/error/500");
			}
		}
	}
}
