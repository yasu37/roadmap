package com.example.roadmap.controller;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.roadmap.component.RedirectUrlHolder;
import com.example.roadmap.entity.Post;
import com.example.roadmap.service.MarkdownContentService;
import com.example.roadmap.service.MarkdownContentService.MarkdownView;
import com.example.roadmap.service.PostService;

@Controller
public class PostController {

	private static final Set<String> SUPPORTED_LANGUAGES = Set.of("zh-cn", "ja-jp");

	private final PostService postService;
	private final MarkdownContentService markdownContentService;
	private final RedirectUrlHolder redirectUrlHolder;

	public PostController(
			PostService postService,
			MarkdownContentService markdownContentService,
			RedirectUrlHolder redirectUrlHolder) {
		this.postService = postService;
		this.markdownContentService = markdownContentService;
		this.redirectUrlHolder = redirectUrlHolder;
	}

	@GetMapping("/{lang}/posts")
	public String listPosts(@PathVariable("lang") String lang, Model model) {
		validateLanguage(lang);

		List<Post> posts = postService.findPublishedPostsByLanguage(lang);

		model.addAttribute("currentLang", lang);
		model.addAttribute("pageTitle", buildListPageTitle(lang));
		model.addAttribute("posts", posts);
		model.addAttribute("otherLang", "zh-cn".equals(lang) ? "ja-jp" : "zh-cn");
		model.addAttribute("otherLangUrl", "/" + ("zh-cn".equals(lang) ? "ja-jp" : "zh-cn"));
		model.addAttribute("topUrl", "/" + lang);

		return "public/post-list";
	}

	@GetMapping("/{lang}/posts/{slug}")
	public String showPost(@PathVariable("lang") String lang,
			@PathVariable("slug") String slug,
			Model model,
			Principal principal,
			HttpServletRequest request) {
		validateLanguage(lang);

		Post post = postService.findPublishedPost(lang, slug);
		boolean loggedIn = principal != null;

		if (!loggedIn) {
			HttpSession session = request.getSession();
			redirectUrlHolder.save(session, request.getRequestURI());
		}

		MarkdownView markdownView = markdownContentService.loadForView(post.getContentRef(), loggedIn);

		String targetLang = "zh-cn".equals(lang) ? "ja-jp" : "zh-cn";
		Post siblingPost = postService.findPublishedSiblingPost(post, targetLang);

		model.addAttribute("currentLang", lang);
		model.addAttribute("pageTitle", post.getTitle());
		model.addAttribute("post", post);
		model.addAttribute("publicHtml", markdownView.publicHtml());
		model.addAttribute("memberOnlyHtml", markdownView.memberOnlyHtml());
		model.addAttribute("hasSummaryBlock", markdownView.hasSummaryBlock());
		model.addAttribute("showMemberOnly", markdownView.showMemberOnly());
		model.addAttribute("showCta", markdownView.showCta());
		model.addAttribute("isLoggedIn", loggedIn);
		model.addAttribute("otherLang", targetLang);
		model.addAttribute("otherLangUrl", siblingPost != null
				? "/" + targetLang + "/posts/" + siblingPost.getSlug()
				: "/" + targetLang + "/posts");
		model.addAttribute("postListUrl", "/" + lang + "/posts");
		model.addAttribute("loginUrl", "/login");
		model.addAttribute("registerUrl", "/register");

		return "public/post-detail";
	}

	private void validateLanguage(String lang) {
		if (!SUPPORTED_LANGUAGES.contains(lang)) {
			throw new IllegalArgumentException("Unsupported language: " + lang);
		}
	}

	private String buildListPageTitle(String lang) {
		if ("ja-jp".equals(lang)) {
			return "記事一覧 | 日本IT内定ロードマップ";
		}
		return "文章列表 | 日本IT内定ロードマップ";
	}
}