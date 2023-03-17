package com.alzzaipo.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoUserInfoDto {
    private final Long kakaoAccountId;
    private final String email;

    @Builder
    public KakaoUserInfoDto(Long kakaoAccountId, String email) {
        this.kakaoAccountId = kakaoAccountId;
        this.email = email;
    }
}
