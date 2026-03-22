package com.example.roadmap.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {

	@Id
	private UUID id;

	@Column(nullable = false, unique = true, length = 50)
	private String name;

	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}
}