package com.example.roadmap.controller;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PublicPageController {

    private static final Set<String> SUPPORTED_LANGUAGES = Set.of("zh-cn", "ja-jp");
    private static final String DEFAULT_LANGUAGE = "zh-cn";

    @GetMapping("/")
    public String redirectRoot() {
        return "redirect:/" + DEFAULT_LANGUAGE;
    }

    @GetMapping("/{lang}")
    public String showTop(@PathVariable("lang") String lang, Model model) {
        if (!SUPPORTED_LANGUAGES.contains(lang)) {
            throw new IllegalArgumentException("Unsupported language: " + lang);
        }

        model.addAttribute("currentLang", lang);
        model.addAttribute("pageTitle", buildPageTitle(lang));
        model.addAttribute("articleListUrl", "/" + lang + "/posts");
        model.addAttribute("otherLang", "zh-cn".equals(lang) ? "ja-jp" : "zh-cn");
        model.addAttribute("otherLangUrl", "/" + ("zh-cn".equals(lang) ? "ja-jp" : "zh-cn"));
        return "public/top";
    }

    private String buildPageTitle(String lang) {
        if ("ja-jp".equals(lang)) {
            return "日本IT内定ロードマップ | 日本語トップ";
        }
        return "日本IT内定ロードマップ | 中文首页";
    }
}