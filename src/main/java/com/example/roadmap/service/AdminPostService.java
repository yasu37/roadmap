package com.example.roadmap.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.roadmap.dto.AdminPostSearchForm;
import com.example.roadmap.entity.Post;
import com.example.roadmap.repository.PostRepository;

@Service
public class AdminPostService {

	private final PostRepository postRepository;

	public AdminPostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	public List<Post> searchPosts(AdminPostSearchForm form) {
		String title = trimToEmpty(form.getTitle());
		String slug = trimToEmpty(form.getSlug());
		String status = trimToNull(form.getStatus());

		if (title.isEmpty() && slug.isEmpty() && status == null) {
			return postRepository.findAllByOrderByUpdatedAtDesc();
		}

		if (title.isEmpty() && slug.isEmpty()) {
			return postRepository.findByStatusOrderByUpdatedAtDesc(status);
		}

		if (status == null) {
			return postRepository.findByTitleContainingIgnoreCaseAndSlugContainingIgnoreCaseOrderByUpdatedAtDesc(
					title, slug);
		}

		return postRepository.findByTitleContainingIgnoreCaseAndSlugContainingIgnoreCaseAndStatusOrderByUpdatedAtDesc(
				title, slug, status);
	}

	private String trimToEmpty(String value) {
		return value == null ? "" : value.trim();
	}

	private String trimToNull(String value) {
		if (value == null) {
			return null;
		}
		String trimmed = value.trim();
		return trimmed.isEmpty() ? null : trimmed;
	}
}