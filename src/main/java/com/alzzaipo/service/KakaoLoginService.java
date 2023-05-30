package com.alzzaipo.service;

import com.alzzaipo.config.MemberPrincipal;
import com.alzzaipo.domain.account.social.SocialAccount;
import com.alzzaipo.dto.account.social.SocialAccountInfo;
import com.alzzaipo.enums.ErrorCode;
import com.alzzaipo.enums.LoginType;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class KakaoLoginService {

    private final SocialAccountService socialAccountService;
    private final JwtUtil jwtUtil;

    @Value("${kakao.restApiKey}")
    private String restApiKey;

    @Value("${kakao.clientSecret}")
    private String clientSecret;

    @Value("${kakao.redirectURI}")
    private String redirectURI;

    @Value("${kakao.adminKey}")
    private String adminKey;

    public String kakaoLogin(String code) throws JsonProcessingException {
        // 인가 코드 검증
        if(code == null || code.equals("")) {
            throw new AppException(ErrorCode.INVALID_KAKAO_AUTH_CODE, "카카오 인가코드를 수신하지 못했습니다.");
        }

        // 인가 코드로 액세스 토큰 요청
        String accessToken = getAccessToken(code);
        if(accessToken == null || accessToken.equals("")) {
            throw new AppException(ErrorCode.INVALID_KAKAO_ACCESS_TOKEN, "카카오 액세스 토큰을 수신하지 못했습니다.");
        }

        // 액세스 토큰으로 카카오 프로필 정보(이메일, 닉네임) 조회
        SocialAccountInfo accountInfo;
        try {
             accountInfo = getAccountInfo(accessToken);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "카카오 프로필 정보 조회 실패");
        }

        // 등록되지 않은 계정인 경우 가입 처리
        socialAccountService.registerIfNeeded(accountInfo, LoginType.KAKAO);

        // 등록된 소셜 계정 조회
        SocialAccount socialAccount = socialAccountService.findByEmailAndLoginType(accountInfo.getEmail(), LoginType.KAKAO)
                        .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED, "카카오 계정 조회 실패"));

        // JWT 발급
        String jwt = jwtUtil.createToken(socialAccount.getMember().getId(), LoginType.KAKAO);

        // JWT 반환
        return jwt;
    }

    public void connect(MemberPrincipal memberInfo, String code) throws JsonProcessingException {
        // 인가 코드 검증
        if(code == null || code.equals("")) {
            throw new AppException(ErrorCode.INVALID_KAKAO_AUTH_CODE, "카카오 인가코드를 수신하지 못했습니다.");
        }

        // 인가 코드로 액세스 토큰 요청
        String accessToken = getAccessToken(code);
        if(accessToken == null || accessToken.equals("")) {
            throw new AppException(ErrorCode.INVALID_KAKAO_ACCESS_TOKEN, "카카오 액세스 토큰을 수신하지 못했습니다.");
        }

        // 액세스 토큰으로 카카오 프로필 정보(이메일, 닉네임) 조회
        SocialAccountInfo socialAccountInfo;
        try {
            socialAccountInfo = getAccountInfo(accessToken);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "카카오 프로필 정보 조회 실패");
        }

        // 기존 회원 정보에 새로운 소셜 계정 연동 처리
        socialAccountService.connectNewAccount(memberInfo.getMemberId(), socialAccountInfo, LoginType.KAKAO);
    }

    /* 인가 코드로 액세스 토큰 받기 */
    private String getAccessToken(String code) throws JsonProcessingException {
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // Body에 인자 설정
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", restApiKey);
        body.add("redirect_uri", redirectURI);
        body.add("code", code);
        body.add("client_secret", clientSecret);

        // 요청 엔티티 생성
        HttpEntity<LinkedMultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // POST 요청 전송
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, requestEntity, String.class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        // Response Null 체크
        if(response == null) {
            return "";
        }

        // Response Body Null 체크
        String responseBody = response.getBody();
        if(responseBody == null || responseBody.equals("")) {
            return "";
        }

        // 액세스 토큰 추출
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String accessToken = jsonNode.get("access_token").asText();

        // 액세스 토큰 반환
        return accessToken;
    }

    /* 액세스 토큰으로 계정 정보 획득 */
    private SocialAccountInfo getAccountInfo(String accessToken) throws JsonProcessingException {
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 엔티티 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

        // GET Request 전송
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, requestEntity, String.class);

        // JSON 파싱 처리
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        // 카카오 계정의 uid 및 닉네임 획득
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("kakao_account").get("profile").get("nickname").asText();

        // 사용자 정보 반환
        return new SocialAccountInfo(email, nickname);
    }
}
