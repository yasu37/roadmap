package com.example.roadmap.infrastructure.markdown;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

@Component
public class MarkdownLoader {

	public String load(String contentRef) {
		Path path = Path.of("content", contentRef);
		try {
			return Files.readString(path, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new IllegalArgumentException("Markdown file not found: " + path, e);
		}
	}
}