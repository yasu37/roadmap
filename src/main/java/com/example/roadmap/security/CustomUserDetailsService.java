package com.example.roadmap.security;

import java.util.List;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.roadmap.entity.Role;
import com.example.roadmap.entity.User;
import com.example.roadmap.entity.UserRole;
import com.example.roadmap.repository.RoleRepository;
import com.example.roadmap.repository.UserRepository;
import com.example.roadmap.repository.UserRoleRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;
	private final RoleRepository roleRepository;

	public CustomUserDetailsService(
			UserRepository userRepository,
			UserRoleRepository userRoleRepository,
			RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email.trim().toLowerCase())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		if (!user.isEnabled() || !user.isEmailVerified()) {
			throw new DisabledException("User is not enabled");
		}

		List<SimpleGrantedAuthority> authorities = userRoleRepository.findByUserId(user.getId())
				.stream()
				.map(UserRole::getRoleId)
				.map(roleId -> roleRepository.findById(roleId)
						.map(Role::getName)
						.orElseThrow(() -> new IllegalStateException("Role not found: " + roleId)))
				.map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
				.toList();

		return org.springframework.security.core.userdetails.User
				.withUsername(user.getEmail())
				.password(user.getPasswordHash())
				.authorities(authorities)
				.accountLocked(false)
				.disabled(!user.isEnabled())
				.build();
	}
}