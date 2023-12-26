package com.alzzaipo.common.token.application.port.out;

import com.alzzaipo.common.Id;

public interface SaveRefreshTokenPort {

	void save(String token, Id memberId);
}
