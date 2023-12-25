package com.alzzaipo.member.adapter.in.web.dto;

import com.alzzaipo.common.token.domain.TokenInfo;
import lombok.Getter;

@Getter
public class TokenResponse {

	private final String accessToken;
	private final String refreshToken;

	public TokenResponse(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public static TokenResponse build(TokenInfo tokenInfo) {
		return new TokenResponse(tokenInfo.getAccessToken(), tokenInfo.getRefreshToken());
	}
}
