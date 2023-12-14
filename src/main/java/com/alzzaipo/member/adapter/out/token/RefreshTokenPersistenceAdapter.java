package com.alzzaipo.member.adapter.out.token;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.jwt.JwtProperties;
import com.alzzaipo.member.application.port.out.SaveRefreshTokenPort;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter implements SaveRefreshTokenPort {

	private final RedisTemplate<String, String> redisTemplate;
	private final JwtProperties jwtProperties;

	@Override
	public void save(String token, Uid memberId) {
		Long expirationTimeMillis = jwtProperties.getRefreshTokenExpirationTimeMillis();
		redisTemplate.opsForValue().set(token, memberId.toString(), expirationTimeMillis, TimeUnit.MILLISECONDS);
	}
}
