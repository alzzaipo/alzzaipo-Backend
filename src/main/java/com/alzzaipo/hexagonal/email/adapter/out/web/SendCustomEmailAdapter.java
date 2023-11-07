package com.alzzaipo.hexagonal.email.adapter.out.web;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.email.application.port.in.SendCustomEmail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendCustomEmailAdapter implements SendCustomEmail {

    @Value("${smtp.email}")
    private String from;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendCustomEmail(Email to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to.get());
        message.setSubject(subject);
        message.setText(text);

        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("알림 메일 전송 실패 : " + e.getMessage());
        }
    }
}
