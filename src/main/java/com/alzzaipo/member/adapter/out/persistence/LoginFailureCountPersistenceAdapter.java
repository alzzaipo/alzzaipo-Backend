package com.alzzaipo.member.adapter.out.persistence;

import com.alzzaipo.member.application.port.out.account.local.CheckLoginFailureCountLimitReachedPort;
import com.alzzaipo.member.application.port.out.account.local.IncrementLoginFailureCountPort;
import com.alzzaipo.member.application.port.out.account.local.ResetLoginFailureCountPort;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
@RequiredArgsConstructor
public class LoginFailureCountPersistenceAdapter implements CheckLoginFailureCountLimitReachedPort,
    IncrementLoginFailureCountPort,
    ResetLoginFailureCountPort {

    private static final String namespace = "login-failure-count";

    @Value("${cache.expiration-time-millis.login-failure-count}")
    private long EXPIRATION_TIME_MILLIS;

    @Value("${LOGIN-FAILURE-COUNT-LIMIT}")
    private long LOGIN_FAILURE_COUNT_LIMIT;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean checkLimitReached(String clientIP) {
        String loginFailureCount = redisTemplate.opsForValue().get(namespace + clientIP);

        if (loginFailureCount == null) {
            return false;
        }
        return Integer.parseInt(loginFailureCount) >= LOGIN_FAILURE_COUNT_LIMIT;
    }

    @Override
    public void increment(String clientIP) {
        String key = namespace + clientIP;

        String loginFailureCount = redisTemplate.opsForValue().get(key);
        if (loginFailureCount == null) {
            redisTemplate.opsForValue().set(key, "1", EXPIRATION_TIME_MILLIS, TimeUnit.MILLISECONDS);
            return;
        }

        // 임계값 이상인 경우 카운트 값을 유지하여 오버플로우를 방지
        int count = Integer.parseInt(loginFailureCount);
        if (count < LOGIN_FAILURE_COUNT_LIMIT) {
            redisTemplate.opsForValue()
                .set(key, String.valueOf(count + 1), EXPIRATION_TIME_MILLIS, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void reset(String clientIP) {
        redisTemplate.delete(namespace + clientIP);
    }
}
