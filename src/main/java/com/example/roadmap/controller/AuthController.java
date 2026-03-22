package com.example.roadmap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.roadmap.dto.RegisterForm;
import com.example.roadmap.service.AuthService;

@Controller
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@GetMapping("/login")
	public String showLogin(Model model) {
		model.addAttribute("pageTitle", "Login | 日本IT内定ロードマップ");
		return "public/login";
	}

	@GetMapping("/register")
	public String showRegister(Model model) {
		model.addAttribute("pageTitle", "Register | 日本IT内定ロードマップ");
		model.addAttribute("registerForm", new RegisterForm());
		return "public/register";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute RegisterForm registerForm,
			RedirectAttributes redirectAttributes) {
		try {
			authService.register(registerForm);
			redirectAttributes.addFlashAttribute("message", "会員登録が完了しました。ログインしてください。");
			return "redirect:/login";
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/register";
		}
	}
}