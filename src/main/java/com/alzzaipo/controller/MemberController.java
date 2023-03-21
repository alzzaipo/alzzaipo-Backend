package com.alzzaipo.controller;

import com.alzzaipo.domain.dto.MemberJoinRequestDto;
import com.alzzaipo.domain.dto.MemberLoginRequestDto;
import com.alzzaipo.service.KakaoLoginService;
import com.alzzaipo.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final KakaoLoginService kakaoLoginService;

    @PostMapping("/api/member/join")
    public ResponseEntity<String> join(@RequestBody MemberJoinRequestDto dto) {
        memberService.join(dto.getAccountId(), dto.getAccountPassword(), dto.getEmail(), dto.getNickname());
        return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/api/member/login")
    public ResponseEntity<String> login(@RequestBody MemberLoginRequestDto dto) {
        String token = memberService.login(dto.getAccountId(), dto.getAccountPassword());
        return ResponseEntity.ok().body(token);
    }

    @GetMapping("/kakao/login")
    public void redirectClientToKakaoLoginPage(HttpServletResponse response) throws IOException {
        String authCodeRequestUrl = kakaoLoginService.getAuthCodeRequestUrl();
        response.sendRedirect(authCodeRequestUrl);
    }

    @GetMapping("/kakao_callback")
    public ResponseEntity<String> kakaoCallback(@RequestParam("code") String code) throws JsonProcessingException {
        String token = kakaoLoginService.kakaoLogin(code);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/api/member/kakao_logout")
    public ResponseEntity<String> kakaoLogout(@AuthenticationPrincipal String accountId) {
        kakaoLoginService.kakaoLogout(accountId);
        return ResponseEntity.ok().body("로그아웃이 완료되었습니다.");
    }
}
