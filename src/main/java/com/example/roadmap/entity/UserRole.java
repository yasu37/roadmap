package com.example.roadmap.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_roles")
@IdClass(UserRole.UserRoleId.class)
public class UserRole {

	@Id
	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Id
	@Column(name = "role_id", nullable = false)
	private UUID roleId;

	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public UUID getRoleId() {
		return roleId;
	}

	public void setRoleId(UUID roleId) {
		this.roleId = roleId;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public static class UserRoleId implements Serializable {
		private UUID userId;
		private UUID roleId;

		public UserRoleId() {
		}

		public UserRoleId(UUID userId, UUID roleId) {
			this.userId = userId;
			this.roleId = roleId;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof UserRoleId that))
				return false;
			return Objects.equals(userId, that.userId) && Objects.equals(roleId, that.roleId);
		}

		@Override
		public int hashCode() {
			return Objects.hash(userId, roleId);
		}
	}
}