package com.example.roadmap.infrastructure.markdown;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@Component
public class MarkdownRenderer {

	private final Parser parser = Parser.builder().build();
	private final HtmlRenderer renderer = HtmlRenderer.builder().build();

	public String render(String markdown) {
		return renderer.render(parser.parse(markdown == null ? "" : markdown));
	}
}