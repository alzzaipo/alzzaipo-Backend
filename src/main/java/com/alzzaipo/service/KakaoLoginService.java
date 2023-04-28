package com.alzzaipo.service;

import com.alzzaipo.domain.dto.KakaoUserInfoDto;
import com.alzzaipo.domain.kakaoAccountInfo.KakaoAccountInfo;
import com.alzzaipo.domain.kakaoAccountInfo.KakaoAccountInfoRepository;
import com.alzzaipo.domain.member.Member;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.exception.ErrorCode;
import com.alzzaipo.utils.JwtUtil;
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

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class KakaoLoginService {

    private final MemberService memberService;
    private final KakaoAccountInfoRepository kakaoAccountInfoRepository;
    private final JwtUtil jwtUtil;

    @Value("${kakao.restApiKey}")
    private String restApiKey;

    @Value("${kakao.clientSecret}")
    private String clientSecret;

    @Value("${kakao.redirectURI}")
    private String redirectURI;

    @Value("${kakao.adminKey}")
    private String adminKey;

    public String getAuthCodeRequestUrl() {
        return "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + restApiKey + "&redirect_uri=" + redirectURI;
    }

    public String kakaoLogin(String code) throws JsonProcessingException {
        // 인가 코드 확인
        if(code == null || code.equals("")) {
            throw new AppException(ErrorCode.INVALID_KAKAO_AUTH_CODE, "카카오 인가코드를 수신하지 못했습니다.");
        }

        // 인가 코드로 액세스 토큰 요청
        String accessToken = getAccessToken(code);
        if(accessToken == null || accessToken.equals("")) {
            throw new AppException(ErrorCode.INVALID_KAKAO_ACCESS_TOKEN, "카카오 액세스 토큰을 수신하지 못했습니다");
        }

        // 액세스 토큰으로 카카오 사용자 정보 요청
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 카카오 이메일 정보로 Member 엔티티 조회, 없으면 이메일 정보 반환하며 401 Unauthorized Error
        Member member = findMemberByEmail(kakaoUserInfo.getEmail());

        // 회원 계정과 카카오 계정 연동 확인, 없으면 카카오 계정 정보 등록
        registerKakaoAccountIfNeeded(member, kakaoUserInfo.getKakaoAccountId());

        // JWT 발급
        String token = jwtUtil.createToken(member.getAccountId());
        return token;
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

    /* 액세스 토큰으로 사용자 정보 가져오기*/
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 엔티티 설정
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

        // GET 요청 전송
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, requestEntity, String.class);

        // Body에서 사용자 정보 추출
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        // 카카오 고유 계정 ID(추후 사용자 정보 동의 요청, 로그아웃 요청에 사용)
        long kakaoAccountId = jsonNode.get("id").asLong();
        // 카카오 계정 이메일
        String email = jsonNode.get("kakao_account").get("email").asText();

        // 사용자 정보를 KakaoUserInfoDto에 담아서 반환
        KakaoUserInfoDto kakaoUserInfo = KakaoUserInfoDto.builder()
                .kakaoAccountId(kakaoAccountId)
                .email(email)
                .build();
        return kakaoUserInfo;
    }

    // 카카오 계정 이메일로 Member 조회 후 엔티티 반환
    private Member findMemberByEmail(String email) {

        Member member = memberService.findMemberByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.KAKAO_ACCOUNT_NOT_REGISTERED, email));

        return member;
    }

    // 카카오 계정 ID 등록 여부 확인, 없으면 등록
    private void registerKakaoAccountIfNeeded(Member member, Long kakaoAccountId) {
        Optional<Long> findResult
                = kakaoAccountInfoRepository.findKakaoAccountIdByMemberAccountId(member.getAccountId());

        if(findResult.isEmpty()) {
            kakaoAccountInfoRepository.save(new KakaoAccountInfo(kakaoAccountId, member));
        }
    }

    // 카카오 계정 로그아웃 처리
    public void kakaoLogout(String memberAccountId) {

        Long kakaoAccountId = kakaoAccountInfoRepository.findKakaoAccountIdByMemberAccountId(memberAccountId)
                .orElseThrow(() -> new AppException(ErrorCode.KAKAO_ACCOUNT_NOT_REGISTERED,
                        "로그아웃 실패 - 등록된 카카오 계정정보가 없습니다."));

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Authorization", "KakaoAK " + adminKey);

        // Body에 인자 설정
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", String.valueOf(kakaoAccountId));

        // 요청 엔티티 생성
        HttpEntity<LinkedMultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // POST 요청 전송
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange("https://kapi.kakao.com/v1/user/logout", HttpMethod.POST, requestEntity, String.class);
    }
}
