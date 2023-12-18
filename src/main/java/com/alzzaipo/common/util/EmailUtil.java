package com.alzzaipo.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

	private static String senderEmail;

	public EmailUtil(@Value("${smtp.email}") String senderEmail) {
		EmailUtil.senderEmail = senderEmail;
	}

	public static SimpleMailMessage createMailMessage(String to, String subject, String text) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(senderEmail);
		simpleMailMessage.setTo(to);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(text);
		return simpleMailMessage;
	}
}
