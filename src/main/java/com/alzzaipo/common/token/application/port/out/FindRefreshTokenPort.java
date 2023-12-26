package com.alzzaipo.common.token.application.port.out;

import com.alzzaipo.common.Id;
import java.util.Optional;

public interface FindRefreshTokenPort {

	Optional<Id> find(String refreshToken);
}
