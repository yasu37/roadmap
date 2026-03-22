package com.example.roadmap.controller;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.roadmap.entity.Post;
import com.example.roadmap.service.PostService;

@Controller
public class PostController {

	private static final Set<String> SUPPORTED_LANGUAGES = Set.of("zh-cn", "ja-jp");

	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping("/{lang}/posts")
	public String listPosts(@PathVariable("lang") String lang, Model model) {
		if (!SUPPORTED_LANGUAGES.contains(lang)) {
			throw new IllegalArgumentException("Unsupported language: " + lang);
		}

		List<Post> posts = postService.findPublishedPostsByLanguage(lang);

		model.addAttribute("currentLang", lang);
		model.addAttribute("pageTitle", buildPageTitle(lang));
		model.addAttribute("posts", posts);
		model.addAttribute("otherLang", "zh-cn".equals(lang) ? "ja-jp" : "zh-cn");
		model.addAttribute("otherLangUrl", "/" + ("zh-cn".equals(lang) ? "ja-jp" : "zh-cn"));
		model.addAttribute("topUrl", "/" + lang);

		return "public/post-list";
	}

	private String buildPageTitle(String lang) {
		if ("ja-jp".equals(lang)) {
			return "記事一覧 | 日本IT内定ロードマップ";
		}
		return "文章列表 | 日本IT内定ロードマップ";
	}
}