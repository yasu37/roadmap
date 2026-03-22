package com.example.roadmap.service;

import org.springframework.stereotype.Service;

import com.example.roadmap.infrastructure.markdown.MarkdownLoader;
import com.example.roadmap.infrastructure.markdown.MarkdownRenderer;
import com.example.roadmap.infrastructure.markdown.SummaryBlockParser;
import com.example.roadmap.infrastructure.markdown.SummaryBlockParser.ParsedMarkdown;

@Service
public class MarkdownContentService {

	private final MarkdownLoader markdownLoader;
	private final SummaryBlockParser summaryBlockParser;
	private final MarkdownRenderer markdownRenderer;

	public MarkdownContentService(
			MarkdownLoader markdownLoader,
			SummaryBlockParser summaryBlockParser,
			MarkdownRenderer markdownRenderer) {
		this.markdownLoader = markdownLoader;
		this.summaryBlockParser = summaryBlockParser;
		this.markdownRenderer = markdownRenderer;
	}

	public MarkdownView loadForPublicView(String contentRef, boolean loggedIn) {
		String raw = markdownLoader.load(contentRef);
		ParsedMarkdown parsed = summaryBlockParser.parse(raw);

		String publicHtml = markdownRenderer.render(parsed.publicPart());
		String memberOnlyHtml = loggedIn
				? markdownRenderer.render(parsed.memberOnlyPart())
				: "";

		boolean showMemberOnly = loggedIn && parsed.hasSummaryBlock() && !parsed.memberOnlyPart().isBlank();
		boolean showCta = !loggedIn && parsed.hasSummaryBlock() && !parsed.memberOnlyPart().isBlank();

		return new MarkdownView(
				publicHtml,
				memberOnlyHtml,
				parsed.hasSummaryBlock(),
				showMemberOnly,
				showCta);
	}

	public record MarkdownView(
			String publicHtml,
			String memberOnlyHtml,
			boolean hasSummaryBlock,
			boolean showMemberOnly,
			boolean showCta) {
	}
}