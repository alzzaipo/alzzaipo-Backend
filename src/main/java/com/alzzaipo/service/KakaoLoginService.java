package com.alzzaipo.service;

import com.alzzaipo.config.KakaoLoginConfig;
import com.alzzaipo.web.domain.Member.Member;
import com.alzzaipo.web.dto.KakaoUserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.alzzaipo.config.SessionConfig;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoLoginService {

    private final String restApiKey = KakaoLoginConfig.getRestAPIKey();
    private final String redirectURI = KakaoLoginConfig.getRedirectURI();
    private final String clientSecret = KakaoLoginConfig.getClientSecret();
    private final MemberService memberService;

    public String getAuthCodeRequestUrl() {
        return "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + restApiKey + "&redirect_uri=" + redirectURI;
    }

    public void kakaoLogin(String code, HttpSession session) throws JsonProcessingException {
        log.info("kakaoLogin() - code:" + code);

        // 인가 코드로 액세스 토큰 요청
        String accessToken = getAccessToken(code);
        log.info("kakaoLogin() - accessToken:" + accessToken);

        // 액세스 토큰으로 카카오 사용자 정보 요청
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
        log.info("kakaoLogin() - nickname:" + kakaoUserInfo.getNickname() + " / email:" + kakaoUserInfo.getEmail());

        // 카카오 사용자 정보로 회원 등록여부 조회, 없으면 디비에 새로 등록
        Member member = registerKakaoUserIfNeed(kakaoUserInfo);

        session.setAttribute(SessionConfig.accessToken, accessToken);
        session.setAttribute(SessionConfig.memberId, member.getId());
        session.setAttribute(SessionConfig.nickname, member.getNickname());
    }


    /* 인가 코드로 액세스 토큰 받기 */
    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", restApiKey);
        body.add("redirect_uri", redirectURI);
        body.add("code", code);
        body.add("client_secret", clientSecret);

        // HTTP Request Entity
        HttpEntity<LinkedMultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // HTTP POST Request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, requestEntity, String.class);

        // HTTP Response (JSON) -> Access Token
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String accessToken = jsonNode.get("access_token").asText();

        return accessToken;
    }

    /* 액세스 토큰으로 사용자 정보 가져오기*/
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Entity
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

        // HTTP GET Request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, requestEntity, String.class);

        // HTTP Response (JSON) -> Kakao User Info(id, email, nickname)
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("kakao_account").get("profile").get("nickname").asText();

        // Kakao User Info -> KakaoUserInfoDto
        KakaoUserInfoDto kakaoUserInfo = KakaoUserInfoDto.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .build();

        return kakaoUserInfo;
    }

    // 가입이 안되어 있으면 가입처리, Member 엔티티 반환
    private Member registerKakaoUserIfNeed(KakaoUserInfoDto kakaoUserInfo) {
        String nickname = kakaoUserInfo.getNickname();
        String email = kakaoUserInfo.getEmail();

        Member member = memberService.findMemberByEmail(email)
                .orElseGet(() -> memberService.save(new Member(nickname, email)));

        return member;
    }

    public void kakaoLogout(HttpSession session) throws JsonProcessingException {
        String accessToken = (String) session.getAttribute(SessionConfig.accessToken);

        // HTTP Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Authorization", "Bearer " + accessToken);

        // HTTP Request Entity
        HttpEntity<LinkedMultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

        // HTTP POST Request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange("https://kapi.kakao.com/v1/user/logout", HttpMethod.POST, requestEntity, String.class);
    }

}
