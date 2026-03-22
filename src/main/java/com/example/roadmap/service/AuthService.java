package com.example.roadmap.service;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.roadmap.dto.RegisterForm;
import com.example.roadmap.entity.Role;
import com.example.roadmap.entity.User;
import com.example.roadmap.entity.UserRole;
import com.example.roadmap.repository.RoleRepository;
import com.example.roadmap.repository.UserRepository;
import com.example.roadmap.repository.UserRoleRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserRoleRepository userRoleRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthService(
			UserRepository userRepository,
			RoleRepository roleRepository,
			UserRoleRepository userRoleRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.userRoleRepository = userRoleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void register(RegisterForm form) {
		String email = form.getEmail() == null ? "" : form.getEmail().trim().toLowerCase();
		String password = form.getPassword() == null ? "" : form.getPassword();
		String confirmPassword = form.getConfirmPassword() == null ? "" : form.getConfirmPassword();

		if (email.isBlank()) {
			throw new IllegalArgumentException("メールアドレスを入力してください。");
		}
		if (password.length() < 8) {
			throw new IllegalArgumentException("パスワードは8文字以上で入力してください。");
		}
		if (!password.equals(confirmPassword)) {
			throw new IllegalArgumentException("確認用パスワードが一致しません。");
		}
		if (userRepository.existsByEmail(email)) {
			throw new IllegalArgumentException("このメールアドレスは既に登録されています。");
		}

		Role userRole = roleRepository.findByName("USER")
				.orElseThrow(() -> new IllegalStateException("USER role not found"));

		OffsetDateTime now = OffsetDateTime.now();

		User user = new User();
		user.setId(UUID.randomUUID());
		user.setEmail(email);
		user.setPasswordHash(passwordEncoder.encode(password));
		user.setEmailVerified(true);
		user.setEnabled(true);
		user.setCreatedAt(now);
		user.setUpdatedAt(now);
		userRepository.save(user);

		UserRole link = new UserRole();
		link.setUserId(user.getId());
		link.setRoleId(userRole.getId());
		link.setCreatedAt(now);
		userRoleRepository.save(link);
	}
}