package com.alzzaipo.web.controller;

import com.alzzaipo.config.SessionConfig;
import com.alzzaipo.service.KakaoLoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/login/kakao")
    public String requestAuthorizationCode() {
        String authCodeRequestUrl = kakaoLoginService.getAuthCodeRequestUrl();
        return "redirect:" + authCodeRequestUrl;
    }

    @GetMapping("/kakao_callback")
    public String kakaoLogin(@RequestParam("code") String code, HttpSession session, HttpServletResponse response) throws JsonProcessingException {
        kakaoLoginService.kakaoLogin(code, session);

        String sessionAccessToken = (String)session.getAttribute(SessionConfig.accessToken);
        Long sessionMemberId = (Long) session.getAttribute(SessionConfig.memberId);

        log.info("session established - memberId:" + sessionMemberId + " / accessToken:" + sessionAccessToken);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String kakaoLogout(HttpSession session) throws JsonProcessingException {
        // 액세스 토큰 및 리프레시 토큰 만료 요청
        kakaoLoginService.kakaoLogout(session);

        // 사용자 세션 무효화
        if(session != null) {
            log.info("Kakao User Logout. memberId=" + session.getAttribute(SessionConfig.memberId));
            session.invalidate();
        }

        return "index";
    }
}
