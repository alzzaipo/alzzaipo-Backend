package com.alzzaipo.common.token.application.port.out;

public interface RenewRefreshTokenPort {

	void renew(String oldRefreshToken, String newRefreshToken);
}
