package com.example.roadmap.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "posts")
public class Post {

	@Id
	private UUID id;

	@Column(nullable = false, length = 255)
	private String slug;

	@Column(nullable = false, length = 20)
	private String language;

	@Column(name = "translation_group_id", nullable = false)
	private UUID translationGroupId;

	@Column(nullable = false, length = 255)
	private String title;

	@Column(columnDefinition = "text")
	private String excerpt;

	@Column(nullable = false, length = 20)
	private String status;

	@Column(nullable = false, length = 20)
	private String visibility;

	@Column(name = "content_ref", nullable = false, length = 500)
	private String contentRef;

	@Column(name = "published_at")
	private OffsetDateTime publishedAt;

	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;

	public UUID getId() {
		return id;
	}

	public String getSlug() {
		return slug;
	}

	public String getLanguage() {
		return language;
	}

	public UUID getTranslationGroupId() {
		return translationGroupId;
	}

	public String getTitle() {
		return title;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public String getStatus() {
		return status;
	}

	public String getVisibility() {
		return visibility;
	}

	public String getContentRef() {
		return contentRef;
	}

	public OffsetDateTime getPublishedAt() {
		return publishedAt;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}
}