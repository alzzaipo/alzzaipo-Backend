package com.alzzaipo.web.controller;

import com.alzzaipo.config.SessionConfig;
import com.alzzaipo.service.KakaoLoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public String kakaoLogin(@RequestParam("code") String code, HttpSession session, HttpServletRequest request) throws JsonProcessingException {
        kakaoLoginService.kakaoLogin(code, session);

        String sessionAccessToken = (String)session.getAttribute(SessionConfig.accessToken);
        Long sessionMemberId = (Long) session.getAttribute(SessionConfig.memberId);

        log.info("session established - memberId:" + sessionMemberId + " / accessToken:" + sessionAccessToken);

        String referer = request.getHeader("Referer");
        if (referer != null && referer.contains("://")) {
            String[] parts = referer.split("://");
            String protocol = parts[0];
            String domain = parts[1].split("/", 2)[0];
            String URL = protocol + "://" + domain;

            if(!domain.contains("alzzaipo.com")) {
                log.info("X-site domain has connected : " + URL);
                return "redirect:" + URL;
            }
        }

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
