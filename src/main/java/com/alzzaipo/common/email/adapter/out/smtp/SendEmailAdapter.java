package com.alzzaipo.common.email.adapter.out.smtp;

import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.common.email.port.out.smtp.SendCustomEmailPort;
import com.alzzaipo.common.email.port.out.smtp.SendEmailVerificationCodePort;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.common.util.EmailUtil;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendEmailAdapter implements SendCustomEmailPort, SendEmailVerificationCodePort {

	@Value("${smtp.email}")
	private String from;

	private final JavaMailSender javaMailSender;

	@Override
	public void send(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);

		try {
			javaMailSender.send(message);
		} catch (Exception e) {
			log.error("알림 메일 전송 실패 : " + e.getMessage());
		}
	}

	@Override
	public String sendVerificationCode(String email) {
		String verificationCode = generateEmailVerificationCode();
		SimpleMailMessage simpleMailMessage = EmailUtil.createMailMessage(email,
			"[알짜공모주] 이메일 인증코드",
			"이메일 인증코드 : " + verificationCode);

		try {
			javaMailSender.send(simpleMailMessage);
		} catch (MailException e) {
			log.error("MailException : {}", e.getMessage());
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송 실패");
		}

		return verificationCode;
	}

	private String generateEmailVerificationCode() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder verificationCode = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < EmailVerificationCode.length; i++) {
			int index = random.nextInt(characters.length());
			verificationCode.append(characters.charAt(index));
		}

		return verificationCode.toString();
	}
}
