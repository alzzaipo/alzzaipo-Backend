package com.alzzaipo.common.jwt;

import lombok.Getter;

@Getter
public class TokenInfo {

	private final String accessToken;
	private final String RefreshToken;

	public TokenInfo(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		RefreshToken = refreshToken;
	}
}
