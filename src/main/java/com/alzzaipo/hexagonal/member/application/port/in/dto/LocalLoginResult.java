package com.alzzaipo.hexagonal.member.application.port.in.dto;

import lombok.Getter;

@Getter
public class LocalLoginResult {

    private final boolean success;
    private final String token;

    public LocalLoginResult(boolean success, String token) {
        this.success = success;
        this.token = token;
    }

    public static LocalLoginResult getFailedResult() {
        return new LocalLoginResult(false, "");
    }
}
