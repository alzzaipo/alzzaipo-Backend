package com.alzzaipo.web.dto;

public class LoginStatusDto {
    Boolean status;
    String nickname;

    public LoginStatusDto(Boolean status, String nickname) {
        this.status = status;
        this.nickname = nickname;
    }
}
