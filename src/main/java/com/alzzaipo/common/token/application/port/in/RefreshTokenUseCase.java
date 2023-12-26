package com.alzzaipo.common.token.application.port.in;

import com.alzzaipo.common.token.domain.TokenInfo;

public interface RefreshTokenUseCase {

	TokenInfo refresh(String refreshToken);
}
