package com.alzzaipo.common.token.adapter.out.persistence;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.common.token.domain.TokenProperties;
import com.alzzaipo.common.token.application.port.out.FindRefreshTokenPort;
import com.alzzaipo.common.token.application.port.out.RenewRefreshTokenPort;
import com.alzzaipo.common.token.application.port.out.SaveRefreshTokenPort;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter implements SaveRefreshTokenPort,
	FindRefreshTokenPort,
	RenewRefreshTokenPort {

	private final RedisTemplate<String, String> redisTemplate;
	private final TokenProperties tokenProperties;

	@Override
	public void save(String token, Uid memberId) {
		Long expirationTimeMillis = tokenProperties.getRefreshTokenExpirationTimeMillis();
		redisTemplate.opsForValue().set(token, memberId.toString(), expirationTimeMillis, TimeUnit.MILLISECONDS);
	}

	@Override
	public Optional<Uid> find(String refreshToken) {
		String memberId = redisTemplate.opsForValue().get(refreshToken);
		if (memberId != null) {
			return Optional.of(Uid.fromString(memberId));
		}
		return Optional.empty();
	}

	@Override
	public void renew(String oldRefreshToken, String newRefreshToken) {
		String memberId = redisTemplate.opsForValue().get(oldRefreshToken);
		if (memberId == null) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "Invalid Token");
		}
		redisTemplate.delete(oldRefreshToken);
		redisTemplate.opsForValue().set(newRefreshToken, memberId);
	}
}
