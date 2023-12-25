package com.alzzaipo.common.token.application.service;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.common.token.TokenUtil;
import com.alzzaipo.common.token.domain.TokenInfo;
import com.alzzaipo.common.token.application.port.in.RefreshTokenUseCase;
import com.alzzaipo.common.token.application.port.out.FindRefreshTokenPort;
import com.alzzaipo.common.token.application.port.out.RenewRefreshTokenPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {

	private final FindRefreshTokenPort findRefreshTokenPort;
	private final RenewRefreshTokenPort renewRefreshTokenPort;

	@Override
	public TokenInfo refresh(String refreshToken) {
		validateToken(refreshToken);

		Uid memberId = findRefreshTokenPort.find(refreshToken)
			.orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "Invalid Token"));

		TokenInfo tokenInfo = TokenUtil.refreshToken(memberId, refreshToken);
		renewRefreshTokenPort.renew(refreshToken, tokenInfo.getRefreshToken());
		return tokenInfo;
	}

	private void validateToken(String refreshToken) {
		if (!TokenUtil.validate(refreshToken)) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "Invalid Token");
		}
	}
}
