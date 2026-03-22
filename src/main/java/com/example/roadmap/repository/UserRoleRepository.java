package com.example.roadmap.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.roadmap.entity.UserRole;
import com.example.roadmap.entity.UserRole.UserRoleId;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
	List<UserRole> findByUserId(UUID userId);
}