package com.alzzaipo.domain.dto;

import lombok.Getter;

@Getter
public class LoginStatusDto {
    Boolean status;
    String nickname;

    public LoginStatusDto(Boolean status, String nickname) {
        this.status = status;
        this.nickname = nickname;
    }
}
