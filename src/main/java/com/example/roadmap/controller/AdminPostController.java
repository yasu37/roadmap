package com.example.roadmap.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.roadmap.dto.AdminPostSearchForm;
import com.example.roadmap.entity.Post;
import com.example.roadmap.service.AdminPostService;

@Controller
public class AdminPostController {

	private final AdminPostService adminPostService;

	public AdminPostController(AdminPostService adminPostService) {
		this.adminPostService = adminPostService;
	}

	@GetMapping("/admin/posts")
	public String listAdminPosts(@ModelAttribute AdminPostSearchForm form, Model model) {
		List<Post> posts = adminPostService.searchPosts(form);

		model.addAttribute("pageTitle", "Admin Posts | 日本IT内定ロードマップ");
		model.addAttribute("searchForm", form);
		model.addAttribute("posts", posts);
		model.addAttribute("totalCount", posts.size());

		return "admin/admin-post-list";
	}
}