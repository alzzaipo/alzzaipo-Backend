package com.alzzaipo.controller;

import com.alzzaipo.domain.dto.EmailDto;
import com.alzzaipo.domain.dto.EmailVerificationRequestDto;
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

    // 이메일 가입여부 검증
    // 잘못된 형식 - BAD REQUEST, 이메일 중복 : - CONFLICT
    // 가입 가능한 이메일 - 200 OK
    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailDto dto) {
        emailService.verifyEmail(dto.getEmail());
        return ResponseEntity.ok().body("사용 가능한 이메일 입니다.");
    }

    // 사용자의 이메일로 인증코드 전송
    @PostMapping("/send-authcode")
    public ResponseEntity<String> sendAuthCode(@RequestBody EmailDto dto) {
        emailService.sendAuthCode(dto.getEmail());
        return ResponseEntity.ok().body("인증코드 전송");
    }

    // 인증코드 검증
    // 인증코드 만료 -  BAD REQUEST, 인증코드 불일치 - UNAUTHORIZED
    // 인증코드 일치 - 200 OK
    @PostMapping("/verify-authcode")
    public ResponseEntity<String> verifyAuthCode(@RequestBody EmailVerificationRequestDto dto) {
        emailService.verifyAuthCode(dto.getEmail(), dto.getAuthCode());
        return ResponseEntity.ok().body("인증코드 확인");
    }
}