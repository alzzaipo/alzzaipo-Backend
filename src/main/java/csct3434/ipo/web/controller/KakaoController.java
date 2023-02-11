package csct3434.ipo.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import csct3434.ipo.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public void kakaoLogin(@RequestParam("code") String code) throws JsonProcessingException {
        kakaoService.kakaoLogin(code);
    }
}
