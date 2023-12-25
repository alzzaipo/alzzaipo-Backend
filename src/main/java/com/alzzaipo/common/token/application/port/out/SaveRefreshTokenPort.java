package com.alzzaipo.common.token.application.port.out;

import com.alzzaipo.common.Uid;

public interface SaveRefreshTokenPort {

	void save(String token, Uid memberId);
}
