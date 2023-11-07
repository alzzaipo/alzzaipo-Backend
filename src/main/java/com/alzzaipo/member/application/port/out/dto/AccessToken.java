package com.alzzaipo.member.application.port.out.dto;

public class AccessToken {

    private final String accessToken;

    public AccessToken(String accessToken) {
        this.accessToken = accessToken;
        selfValidate();
    }

    public String get() {
        return accessToken;
    }

    private void selfValidate() {
        if(accessToken == null || accessToken.isBlank()) {
            throw new RuntimeException("액세스 토큰 오류");
        }
    }
}
