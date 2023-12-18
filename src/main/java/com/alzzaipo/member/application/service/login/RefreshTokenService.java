package com.alzzaipo.member.application.service.login;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.common.jwt.JwtUtil;
import com.alzzaipo.common.jwt.TokenInfo;
import com.alzzaipo.member.application.port.in.RefreshTokenUseCase;
import com.alzzaipo.member.application.port.out.RenewRefreshTokenPort;
import com.alzzaipo.member.application.port.out.FindRefreshTokenPort;
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

		checkOwnership(memberId, JwtUtil.getMemberUID(refreshToken));

		TokenInfo tokenInfo = JwtUtil.createToken(memberId, JwtUtil.getLoginType(refreshToken));
		renewRefreshTokenPort.renew(refreshToken, tokenInfo.getRefreshToken());
		return tokenInfo;
	}

	private void validateToken(String refreshToken) {
		if (!JwtUtil.validate(refreshToken)) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "Invalid Token");
		}
	}

	private void checkOwnership(Uid storedMemberId, Uid tokenMemberId) {
		if(!storedMemberId.equals(tokenMemberId)) {
			log.warn("Suspicious Refreshing Token Attempted : original {} / attempted: {}", storedMemberId, tokenMemberId);
			throw new CustomException(HttpStatus.UNAUTHORIZED, "Invalid Token");
		}
	}
}
