package com.example.roadmap.infrastructure.mail;

import org.springframework.stereotype.Component;

@Component
public class ConsoleMailSender {

	public void sendVerificationMail(String to, String verificationUrl) {
		System.out.println("=== Verification Mail ===");
		System.out.println("to: " + to);
		System.out.println("verify: " + verificationUrl);
		System.out.println("=========================");
	}
}