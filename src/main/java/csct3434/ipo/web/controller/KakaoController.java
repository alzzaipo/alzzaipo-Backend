package csct3434.ipo.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import csct3434.ipo.config.SessionConfig;
import csct3434.ipo.service.KakaoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/login/kakao")
    public String requestAuthorizationCode() {
        String authCodeRequestUrl = kakaoService.getAuthCodeRequestUrl();
        return "redirect:" + authCodeRequestUrl;
    }

    @GetMapping("/kakao_callback")
    public String kakaoLogin(@RequestParam("code") String code, HttpSession session) throws JsonProcessingException {
        kakaoService.kakaoLogin(code, session);
        String sessionAccessToken = (String)session.getAttribute(SessionConfig.accessToken);
        String sessionMemberEmail = (String) session.getAttribute(SessionConfig.email);
        log.info("session establisehd - email:" + sessionMemberEmail + " / access token:" + sessionAccessToken);
        return "index";
    }
}
