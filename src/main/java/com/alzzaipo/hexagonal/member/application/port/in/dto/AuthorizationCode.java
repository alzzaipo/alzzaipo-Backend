package com.alzzaipo.hexagonal.member.application.port.in.dto;

public class AuthorizationCode {

    private final String authorizationCode;

    public AuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
        selfValidate();
    }

    public String get() {
        return authorizationCode;
    }

    private void selfValidate() {
        if(authorizationCode == null || authorizationCode.isBlank()) {
            throw new RuntimeException("인가 코드 오류");
        }
    }

}
