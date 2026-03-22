package com.example.roadmap.component;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

@Component
public class RedirectUrlHolder {

	private static final String SESSION_KEY = "redirectAfterLogin";

	public void save(HttpSession session, String url) {
		session.setAttribute(SESSION_KEY, url);
	}

	public String consume(HttpSession session) {
		Object value = session.getAttribute(SESSION_KEY);
		if (value == null) {
			return null;
		}
		session.removeAttribute(SESSION_KEY);
		return value.toString();
	}

	public String peek(HttpSession session) {
		Object value = session.getAttribute(SESSION_KEY);
		return value == null ? null : value.toString();
	}

	public void clear(HttpSession session) {
		session.removeAttribute(SESSION_KEY);
	}
}