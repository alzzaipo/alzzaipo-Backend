package com.alzzaipo.common.email.adapter.out.persistence;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.common.email.port.out.verification.CheckEmailVerificationCodePort;
import com.alzzaipo.common.email.port.out.verification.DeleteEmailVerificationStatusPort;
import com.alzzaipo.common.email.port.out.verification.SaveEmailVerificationCodePort;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
@RequiredArgsConstructor
public class EmailVerificationPersistenceAdapter implements SaveEmailVerificationCodePort,
    CheckEmailVerificationCodePort,
    DeleteEmailVerificationStatusPort {

    @Value("${cache.expiration-time-millis.email-verification}")
    private long EXPIRATION_TIME_MILLIS;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(Email email, EmailVerificationCode verificationCode, EmailVerificationPurpose purpose) {
        String namespace = purpose.getNamespace();

        redisTemplate.opsForValue()
            .set(namespace + email.get(), verificationCode.get(), EXPIRATION_TIME_MILLIS, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean check(Email email, EmailVerificationCode userInputVerificationCode, EmailVerificationPurpose purpose) {
        String namespace = purpose.getNamespace();
        String validVerificationCode = redisTemplate.opsForValue().get(namespace + email.get());
        return validVerificationCode != null && validVerificationCode.equals(userInputVerificationCode.get());
    }

    @Override
    public void delete(Email email, EmailVerificationPurpose purpose) {
        String namespace = purpose.getNamespace();
        redisTemplate.delete(namespace + email.get());
    }

}
