package com.alzzaipo.hexagonal.member.adapter.out.web;

import com.alzzaipo.hexagonal.member.application.port.in.dto.AuthorizationCode;
import com.alzzaipo.hexagonal.member.application.port.out.dto.AccessToken;
import com.alzzaipo.hexagonal.member.application.port.out.oauth.ExchangeKakaoAccessTokenPort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class ExchangeKakaoAccessTokenAdapter implements ExchangeKakaoAccessTokenPort {

    @Value("${kakao.restApiKey}")
    private String restApiKey;

    @Value("${kakao.clientSecret}")
    private String clientSecret;

    @Value("${kakao.redirectURI.login}")
    private String redirectURI;

    @Value("${kakao.URI.accessToken}")
    private String endPoint;

    @Override
    public AccessToken exchangeKakaoAccessToken(AuthorizationCode authorizationCode) {
        String accessToken;

        try {
            accessToken = exchangeAccessToken(authorizationCode.get());
        } catch (IOException e) {
            throw new RuntimeException("카카오 액세스 토큰 교환 오류", e);
        }

        return new AccessToken(accessToken);
    }

    private String exchangeAccessToken(String authorizationCode) throws JsonProcessingException {
        HttpEntity<MultiValueMap<String, String>> httpEntity = createHttpEntity(authorizationCode);
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                endPoint,
                HttpMethod.POST,
                httpEntity,
                String.class);

        if (responseEntity == null || responseEntity.getBody() == null || responseEntity.getBody().isBlank()) {
            return "";
        }

        return objectMapper.readTree(responseEntity.getBody())
                .get("access_token")
                .asText();
    }

    private HttpEntity<MultiValueMap<String, String>> createHttpEntity(String authorizationCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", restApiKey);
        body.add("redirect_uri", redirectURI);
        body.add("code", authorizationCode);
        body.add("client_secret", clientSecret);

        return new HttpEntity<>(body, headers);
    }

}
