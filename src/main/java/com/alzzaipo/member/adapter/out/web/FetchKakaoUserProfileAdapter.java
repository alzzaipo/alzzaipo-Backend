package com.alzzaipo.member.adapter.out.web;

import com.alzzaipo.member.application.port.out.dto.AccessToken;
import com.alzzaipo.member.application.port.out.dto.UserProfile;
import com.alzzaipo.member.application.port.out.oauth.FetchKakaoUserProfilePort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class FetchKakaoUserProfileAdapter implements FetchKakaoUserProfilePort {

    @Value("${kakao.URI.userInfo}")
    private String endPoint;

    @Override
    public UserProfile fetchKakaoUserProfile(AccessToken accessToken) {
        UserProfile userProfile;

        try {
            userProfile = fetchUserProfile(accessToken);
        } catch (IOException e) {
            throw new RuntimeException("카카오 사용자 프로필 조회 오류", e);
        }

        return userProfile;
    }

    private UserProfile fetchUserProfile(AccessToken accessToken) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpEntity<Void> httpEntity = createHttpEntity(accessToken.get());

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                endPoint,
                HttpMethod.GET,
                httpEntity,
                String.class);

        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());

        String nickname = parseNickname(jsonNode);
        String email = parseEmail(jsonNode);

        return new UserProfile(nickname, email);
    }

    private HttpEntity<Void> createHttpEntity(String accessToken) {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return new HttpEntity<>(headers);
    }

    private String parseNickname(JsonNode jsonNode) {
        return jsonNode.get("kakao_account")
                .get("profile")
                .get("nickname")
                .asText();
    }

    private String parseEmail(JsonNode jsonNode) {
        return jsonNode.get("kakao_account")
                .get("email")
                .asText();
    }
}
