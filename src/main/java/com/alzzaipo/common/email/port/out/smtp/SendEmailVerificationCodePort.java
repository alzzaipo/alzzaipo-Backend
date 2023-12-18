package com.alzzaipo.common.email.port.out.smtp;

public interface SendEmailVerificationCodePort {

	String sendVerificationCode(String email);
}
