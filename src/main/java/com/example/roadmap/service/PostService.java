package com.example.roadmap.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.roadmap.entity.Post;
import com.example.roadmap.repository.PostRepository;

@Service
public class PostService {

	private static final String PUBLISHED = "published";

	private final PostRepository postRepository;

	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	public List<Post> findPublishedPostsByLanguage(String lang) {
		return postRepository.findByLanguageAndStatusOrderByPublishedAtDesc(lang, PUBLISHED);
	}
}