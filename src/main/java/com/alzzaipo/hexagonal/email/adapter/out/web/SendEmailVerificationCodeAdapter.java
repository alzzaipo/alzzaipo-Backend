package com.alzzaipo.hexagonal.email.adapter.out.web;

import com.alzzaipo.hexagonal.email.application.port.out.SendEmailVerificationCodePort;
import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.email.domain.EmailVerificationCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class SendEmailVerificationCodeAdapter implements SendEmailVerificationCodePort {

    @Value("${smtp.email}")
    private String senderEmail;

    private final JavaMailSender javaMailSender;

    @Override
    public EmailVerificationCode sendEmailVerificationCode(Email email) {
        EmailVerificationCode emailVerificationCode = generateEmailVerificationCode();

        SimpleMailMessage emailContents = new SimpleMailMessage();
        emailContents.setFrom(senderEmail);
        emailContents.setTo(email.get());
        emailContents.setSubject("[알짜공모주] 이메일 인증코드");
        emailContents.setText("인증코드 : " + emailVerificationCode.get());

        javaMailSender.send(emailContents);

        return emailVerificationCode;
    }

    private EmailVerificationCode generateEmailVerificationCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder verificationCode = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < EmailVerificationCode.length; i++) {
            int index = random.nextInt(characters.length());
            verificationCode.append(characters.charAt(index));
        }

        return new EmailVerificationCode(verificationCode.toString());
    }
}
