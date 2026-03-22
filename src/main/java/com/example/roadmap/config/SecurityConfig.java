package com.example.roadmap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/",
								"/zh-cn",
								"/ja-jp",
								"/zh-cn/posts",
								"/ja-jp/posts",
								"/zh-cn/posts/*",
								"/ja-jp/posts/*",
								"/login",
								"/register",
								"/verify-email",
								"/css/**",
								"/js/**",
								"/images/**")
						.permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/profile").authenticated()
						.anyRequest().permitAll())
				.formLogin(form -> form
						.loginPage("/login")
						.defaultSuccessUrl("/zh-cn", true)
						.permitAll())
				.logout(logout -> logout
						.logoutSuccessUrl("/zh-cn")
						.permitAll())
				.csrf(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}