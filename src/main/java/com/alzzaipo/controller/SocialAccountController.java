package com.alzzaipo.controller;

import com.alzzaipo.config.MemberPrincipal;
import com.alzzaipo.service.KakaoLoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/social")
@RequiredArgsConstructor
@Controller
public class SocialAccountController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/kakao/login")
    public ResponseEntity<Void> kakaoLogin(@RequestParam("code") String code) throws JsonProcessingException {
        String jwt = kakaoLoginService.kakaoLogin(code);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);

        return ResponseEntity.ok().headers(headers).build();
    }

    @GetMapping("/kakao/connect")
    public ResponseEntity<Void> kakaoConnect(@AuthenticationPrincipal MemberPrincipal memberInfo, @RequestParam("code") String code) throws JsonProcessingException {
        kakaoLoginService.connect(memberInfo, code);
        return ResponseEntity.ok().build();
    }
}
