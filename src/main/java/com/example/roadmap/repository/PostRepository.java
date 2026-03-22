package com.example.roadmap.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.roadmap.entity.Post;

public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findByLanguageAndStatusOrderByPublishedAtDesc(String language, String status);

    Optional<Post> findBySlugAndLanguageAndStatus(String slug, String language, String status);

    Optional<Post> findByTranslationGroupIdAndLanguageAndStatus(UUID translationGroupId, String language, String status);
}