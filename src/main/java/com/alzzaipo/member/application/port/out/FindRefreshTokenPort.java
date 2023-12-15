package com.alzzaipo.member.application.port.out;

import com.alzzaipo.common.Uid;
import java.util.Optional;

public interface FindRefreshTokenPort {

	Optional<Uid> find(String refreshToken);
}
