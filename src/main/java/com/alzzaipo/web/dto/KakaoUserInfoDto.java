package com.alzzaipo.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoUserInfoDto {
    private final Long kakaoAccountId;
    private final String email;
    private final String nickname;

    @Builder
    public KakaoUserInfoDto(Long kakaoAccountId, String email, String nickname) {
        this.kakaoAccountId = kakaoAccountId;
        this.email = email;
        this.nickname = nickname;
    }
}
