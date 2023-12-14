package com.alzzaipo.member.application.port.out;

import com.alzzaipo.common.Uid;

public interface SaveRefreshTokenPort {

	void save(String token, Uid memberId);
}
