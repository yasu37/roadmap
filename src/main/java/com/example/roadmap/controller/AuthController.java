package com.example.roadmap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.roadmap.dto.RegisterForm;
import com.example.roadmap.service.AuthService;
import com.example.roadmap.service.AuthService.VerifyResult;

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
			redirectAttributes.addFlashAttribute("message", "登録しました。コンソールに出た認証URLを開いてください。");
			return "redirect:/login";
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/register";
		}
	}

	@GetMapping("/verify-email")
	public String verifyEmail(@RequestParam("token") String token, Model model) {
		model.addAttribute("pageTitle", "Verify Email | 日本IT内定ロードマップ");

		try {
			VerifyResult result = authService.verifyEmail(token);
			if (result == VerifyResult.SUCCESS) {
				model.addAttribute("status", "success");
				model.addAttribute("message", "メール認証が完了しました。ログインしてください。");
			} else {
				model.addAttribute("status", "already_verified");
				model.addAttribute("message", "このアカウントはすでに認証済みです。");
			}
		} catch (IllegalStateException e) {
			model.addAttribute("status", "expired");
			model.addAttribute("message", "認証リンクの有効期限が切れています。");
		} catch (IllegalArgumentException e) {
			model.addAttribute("status", "invalid");
			model.addAttribute("message", "無効な認証リンクです。");
		}

		return "public/verify-email-result";
	}
}