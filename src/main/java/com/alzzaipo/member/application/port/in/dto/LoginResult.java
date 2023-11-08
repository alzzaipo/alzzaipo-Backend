package com.alzzaipo.member.application.port.in.dto;

import lombok.Getter;

@Getter
public class LoginResult {

    private final boolean success;
    private final String token;

    public LoginResult(boolean success, String token) {
        this.success = success;
        this.token = token;
    }

    public static LoginResult getFailedResult() {
        return new LoginResult(false, "");
    }
}
