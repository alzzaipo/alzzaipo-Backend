package com.alzzaipo.controller;

import com.alzzaipo.dto.account.local.*;
import com.alzzaipo.dto.email.EmailDto;
import com.alzzaipo.service.LocalAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@Controller
public class LocalAccountController {

    private final LocalAccountService localAccountService;

    @PostMapping("/verify-account-id")
    public ResponseEntity<String> verifyAccountId(@RequestBody LocalAccountIdDto dto) {
        localAccountService.verifyAccountId(dto);
        return ResponseEntity.ok().body("사용 가능한 아이디 입니다.");
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailDto dto) {
        localAccountService.verifyEmail(dto);
        return ResponseEntity.ok().body("사용 가능한 이메일 입니다.");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody LocalAccountRegisterRequestDto dto) {
        localAccountService.register(dto);
        return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LocalAccountLoginRequestDto dto) {
        String jwt = localAccountService.login(dto);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);

        return ResponseEntity.ok().headers(headers).build();
    }

    @GetMapping("/profile")
    public ResponseEntity<LocalAccountProfileDto> profile(@AuthenticationPrincipal Long memberId) {
        LocalAccountProfileDto localAccountProfileDto = localAccountService.getLocalAccountProfileDto(memberId);
        return ResponseEntity.ok().body(localAccountProfileDto);
    }

}
