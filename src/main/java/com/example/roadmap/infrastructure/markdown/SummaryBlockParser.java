package com.example.roadmap.infrastructure.markdown;

import org.springframework.stereotype.Component;

@Component
public class SummaryBlockParser {

	private static final String MARKER = "<!--SUMMARY-->";

	public ParsedMarkdown parse(String markdown) {
		int index = markdown.indexOf(MARKER);

		if (index < 0) {
			return new ParsedMarkdown(markdown, "", false);
		}

		String publicPart = markdown.substring(0, index).trim();
		String memberOnlyPart = markdown.substring(index + MARKER.length()).trim();

		return new ParsedMarkdown(publicPart, memberOnlyPart, true);
	}

	public record ParsedMarkdown(
			String publicPart,
			String memberOnlyPart,
			boolean hasSummaryBlock) {
	}
}