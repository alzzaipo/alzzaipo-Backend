package com.alzzaipo.member.application.port.out;

public interface RenewRefreshTokenPort {

	void renew(String oldRefreshToken, String newRefreshToken);
}
