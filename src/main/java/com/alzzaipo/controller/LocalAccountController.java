package com.alzzaipo.controller;

import com.alzzaipo.dto.account.local.LocalAccountIdDto;
import com.alzzaipo.dto.email.EmailDto;
import com.alzzaipo.dto.account.local.LocalAccountLoginRequestDto;
import com.alzzaipo.dto.account.local.LocalAccountRegisterRequestDto;
import com.alzzaipo.service.LocalAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@Controller
public class LocalAccountController {

    private final LocalAccountService localAccountService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody LocalAccountRegisterRequestDto dto) {
        localAccountService.register(dto);
        return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LocalAccountLoginRequestDto dto) {
        String token = localAccountService.login(dto);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/verify-account-id")
    public ResponseEntity<String> verifyAccountId(@RequestBody LocalAccountIdDto dto) {
        localAccountService.verifyAccountId(dto);
        return ResponseEntity.ok().body("사용 가능한 아이디 입니다.");
    }

    // 이메일 가입여부 검증
    // 잘못된 형식 - BAD REQUEST, 이메일 중복 : - CONFLICT
    // 가입 가능한 이메일 - 200 OK
    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailDto dto) {
        localAccountService.verifyEmail(dto);
        return ResponseEntity.ok().body("사용 가능한 이메일 입니다.");
    }
}
