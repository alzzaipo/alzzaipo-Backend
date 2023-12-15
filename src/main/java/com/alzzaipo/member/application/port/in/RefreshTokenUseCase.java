package com.alzzaipo.member.application.port.in;

import com.alzzaipo.common.jwt.TokenInfo;

public interface RefreshTokenUseCase {

	TokenInfo refresh(String refreshToken);
}
