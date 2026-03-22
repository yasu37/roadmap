package com.example.roadmap.security;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.roadmap.component.RedirectUrlHolder;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private final RedirectUrlHolder redirectUrlHolder;

	public LoginSuccessHandler(RedirectUrlHolder redirectUrlHolder) {
		this.redirectUrlHolder = redirectUrlHolder;
		setDefaultTargetUrl("/zh-cn");
		setAlwaysUseDefaultTargetUrl(false);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {

		String redirectUrl = redirectUrlHolder.consume(request.getSession(false));

		if (redirectUrl != null && !redirectUrl.isBlank()) {
			getRedirectStrategy().sendRedirect(request, response, redirectUrl);
			return;
		}

		super.onAuthenticationSuccess(request, response, authentication);
	}
}