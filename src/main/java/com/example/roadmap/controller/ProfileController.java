package com.example.roadmap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

	@GetMapping("/profile")
	public String showProfile(Model model) {
		model.addAttribute("pageTitle", "Profile | 日本IT内定ロードマップ");
		return "member/profile";
	}
}