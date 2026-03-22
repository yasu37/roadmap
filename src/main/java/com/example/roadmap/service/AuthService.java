package com.example.roadmap.service;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.roadmap.dto.RegisterForm;
import com.example.roadmap.entity.EmailVerificationToken;
import com.example.roadmap.entity.Role;
import com.example.roadmap.entity.User;
import com.example.roadmap.entity.UserRole;
import com.example.roadmap.infrastructure.mail.ConsoleMailSender;
import com.example.roadmap.repository.EmailVerificationTokenRepository;
import com.example.roadmap.repository.RoleRepository;
import com.example.roadmap.repository.UserRepository;
import com.example.roadmap.repository.UserRoleRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserRoleRepository userRoleRepository;
	private final EmailVerificationTokenRepository emailVerificationTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final ConsoleMailSender consoleMailSender;

	public AuthService(
			UserRepository userRepository,
			RoleRepository roleRepository,
			UserRoleRepository userRoleRepository,
			EmailVerificationTokenRepository emailVerificationTokenRepository,
			PasswordEncoder passwordEncoder,
			ConsoleMailSender consoleMailSender) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.userRoleRepository = userRoleRepository;
		this.emailVerificationTokenRepository = emailVerificationTokenRepository;
		this.passwordEncoder = passwordEncoder;
		this.consoleMailSender = consoleMailSender;
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
		user.setEmailVerified(false);
		user.setEnabled(false);
		user.setCreatedAt(now);
		user.setUpdatedAt(now);
		userRepository.save(user);

		UserRole link = new UserRole();
		link.setUserId(user.getId());
		link.setRoleId(userRole.getId());
		link.setCreatedAt(now);
		userRoleRepository.save(link);

		EmailVerificationToken token = new EmailVerificationToken();
		token.setId(UUID.randomUUID());
		token.setUserId(user.getId());
		token.setToken(UUID.randomUUID().toString());
		token.setExpiresAt(now.plusHours(24));
		token.setCreatedAt(now);
		emailVerificationTokenRepository.save(token);

		String verificationUrl = "http://localhost:8080/verify-email?token=" + token.getToken();
		consoleMailSender.sendVerificationMail(user.getEmail(), verificationUrl);
	}

	@Transactional
	public VerifyResult verifyEmail(String tokenValue) {
		EmailVerificationToken token = emailVerificationTokenRepository.findByToken(tokenValue)
				.orElseThrow(() -> new IllegalArgumentException("invalid"));

		if (token.getExpiresAt().isBefore(OffsetDateTime.now())) {
			throw new IllegalStateException("expired");
		}

		User user = userRepository.findById(token.getUserId())
				.orElseThrow(() -> new IllegalArgumentException("invalid"));

		if (user.isEmailVerified() && user.isEnabled()) {
			return VerifyResult.ALREADY_VERIFIED;
		}

		user.setEmailVerified(true);
		user.setEnabled(true);
		user.setUpdatedAt(OffsetDateTime.now());
		userRepository.save(user);

		emailVerificationTokenRepository.delete(token);
		return VerifyResult.SUCCESS;
	}

	public enum VerifyResult {
		SUCCESS, ALREADY_VERIFIED
	}
}