package com.alzzaipo.common.token.domain;

import lombok.Getter;

@Getter
public class TokenInfo {

	private final String accessToken;
	private final String refreshToken;

	public TokenInfo(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
