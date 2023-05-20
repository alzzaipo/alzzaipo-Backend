package com.alzzaipo.controller;

import com.alzzaipo.dto.email.EmailDto;
import com.alzzaipo.dto.email.EmailVerificationRequestDto;
import com.alzzaipo.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/email")
@RestController
public class EmailController {

    private final EmailService emailService;

    // 사용자의 이메일로 인증코드 전송
    @PostMapping("/send-verification-code")
    public ResponseEntity<String> sendVerificationCode(@RequestBody EmailDto dto) {
        emailService.sendVerificationCode(dto.getEmail());
        return ResponseEntity.ok().body("인증메일 전송 완료");
    }

    // 인증코드 검증
    // 인증코드 만료 - BAD REQUEST, 인증코드 불일치 - UNAUTHORIZED
    // 인증코드 일치 - 200 OK
    @PostMapping("/validate-verification-code")
    public ResponseEntity<String> validateVerificationCode(@RequestBody EmailVerificationRequestDto dto) {
        emailService.validateVerificationCode(dto.getEmail(), dto.getVerificationCode());
        return ResponseEntity.ok().body("인증코드 확인 완료");
    }
}