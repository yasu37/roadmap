package com.example.roadmap.controller;

import java.security.Principal;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.roadmap.component.RedirectUrlHolder;
import com.example.roadmap.dto.ProfileForm;
import com.example.roadmap.entity.User;
import com.example.roadmap.repository.UserRepository;
import com.example.roadmap.service.ProfileService;

@Controller
public class ProfileController {

	private final ProfileService profileService;
	private final UserRepository userRepository;
	private final RedirectUrlHolder redirectUrlHolder;

	public ProfileController(
			ProfileService profileService,
			UserRepository userRepository,
			RedirectUrlHolder redirectUrlHolder) {
		this.profileService = profileService;
		this.userRepository = userRepository;
		this.redirectUrlHolder = redirectUrlHolder;
	}

	@GetMapping("/profile")
	public String showProfile(Model model, Principal principal, HttpSession session) {
		User user = userRepository.findByEmail(principal.getName().trim().toLowerCase())
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		model.addAttribute("pageTitle", "Profile | 日本IT内定ロードマップ");
		model.addAttribute("profileForm", profileService.getProfileForm(user.getId()));
		model.addAttribute("isFirstSetup", redirectUrlHolder.peek(session) != null);

		return "member/profile";
	}

	@PostMapping("/profile")
	public String saveProfile(@ModelAttribute ProfileForm profileForm,
			Principal principal,
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			User user = userRepository.findByEmail(principal.getName().trim().toLowerCase())
					.orElseThrow(() -> new IllegalArgumentException("User not found"));

			profileService.saveProfile(user.getId(), profileForm);
			redirectAttributes.addFlashAttribute("message", "プロフィールを保存しました。");

			String redirectUrl = redirectUrlHolder.consume(session);
			if (redirectUrl != null && !redirectUrl.isBlank()) {
				return "redirect:" + redirectUrl;
			}
			return "redirect:/zh-cn";
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/profile";
		}
	}
}