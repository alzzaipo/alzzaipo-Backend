package com.alzzaipo.controller;

import com.alzzaipo.service.KakaoLoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
public class SocialAccountController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/kakao/login")
    public void redirectToKakaoLoginPage(HttpServletResponse response) throws IOException {
        String authCodeRequestUrl = kakaoLoginService.getAuthCodeRequestUrl();
        response.sendRedirect(authCodeRequestUrl);
    }

    @GetMapping("/kakao_callback")
    public ResponseEntity<String> kakaoLogin(@RequestParam("code") String code) throws JsonProcessingException {
        String jwt = kakaoLoginService.kakaoLogin(code);
        return ResponseEntity.ok().body(jwt);
    }
}
