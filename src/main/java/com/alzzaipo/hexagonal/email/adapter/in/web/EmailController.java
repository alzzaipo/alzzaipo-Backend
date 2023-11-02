package com.alzzaipo.hexagonal.email.adapter.in.web;

import com.alzzaipo.hexagonal.email.application.port.in.SendEmailVerificationCodeUseCase;
import com.alzzaipo.hexagonal.email.application.port.in.VerifyEmailVerificationCodeUseCase;
import com.alzzaipo.hexagonal.email.domain.Email;
import com.alzzaipo.hexagonal.email.domain.EmailVerificationCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final SendEmailVerificationCodeUseCase sendEmailVerificationCodeUseCase;
    private final VerifyEmailVerificationCodeUseCase verifyEmailVerificationCodeUseCase;

    @PostMapping("/send-verification-code")
    public ResponseEntity<String> sendVerificationCode(@RequestBody EmailDto dto) {
        sendEmailVerificationCodeUseCase.sendEmailVerificationCode(new Email(dto.getEmail()));
        return ResponseEntity.ok().body("인증메일 전송 완료");
    }

    @PostMapping("/validate-verification-code")
    public ResponseEntity<String> validateVerificationCode(@RequestBody VerifyEmailVerificationCodeWebRequest dto) {
        Email email = new Email(dto.getEmail());
        EmailVerificationCode verificationCode = new EmailVerificationCode(dto.getVerificationCode());

        if (!verifyEmailVerificationCodeUseCase.verifyEmailVerificationCode(email, verificationCode)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
        }

        return ResponseEntity.ok().body("인증 성공");
    }
}
