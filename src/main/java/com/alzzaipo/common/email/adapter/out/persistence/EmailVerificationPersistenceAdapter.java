package com.alzzaipo.common.email.adapter.out.persistence;

import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.common.email.domain.EmailVerificationStatus;
import com.alzzaipo.common.email.port.out.verification.CheckEmailVerifiedPort;
import com.alzzaipo.common.email.port.out.verification.DeleteEmailVerificationStatusPort;
import com.alzzaipo.common.email.port.out.verification.SaveEmailVerificationCodePort;
import com.alzzaipo.common.email.port.out.verification.VerifyEmailVerificationCodePort;
import com.alzzaipo.common.util.JsonUtil;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerificationPersistenceAdapter implements SaveEmailVerificationCodePort,
	VerifyEmailVerificationCodePort,
	CheckEmailVerifiedPort,
	DeleteEmailVerificationStatusPort {

	@Value("${cache.expiration-time-millis.email-verification}")
	private long EXPIRATION_TIME_MILLIS;

	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public void save(String email, String verificationCode, String subject, EmailVerificationPurpose purpose) {
		String namespace = purpose.getNamespace();
		EmailVerificationStatus initialStatus = new EmailVerificationStatus(subject, false);

		redisTemplate.opsForValue().set(namespace + verificationCode, email,
			EXPIRATION_TIME_MILLIS, TimeUnit.MILLISECONDS);

		redisTemplate.opsForValue().set(namespace + email, JsonUtil.toJson(initialStatus),
			EXPIRATION_TIME_MILLIS * 2, TimeUnit.MILLISECONDS);
	}

	@Override
	public boolean verify(String verificationCode, EmailVerificationPurpose purpose) {
		String namespace = purpose.getNamespace();

		String foundEmail = redisTemplate.opsForValue().get(namespace + verificationCode);
		if (foundEmail == null) {
			return false;
		}

		String jsonVerificationStatus = redisTemplate.opsForValue().get(namespace + foundEmail);
		if (jsonVerificationStatus == null) {
			return false;
		}

		EmailVerificationStatus verificationStatus = JsonUtil.fromJson(jsonVerificationStatus,
			EmailVerificationStatus.class);
		verificationStatus.setVerified(true);
		redisTemplate.opsForValue().set(namespace + foundEmail, JsonUtil.toJson(verificationStatus));
		return true;
	}

	@Override
	public boolean check(String email, String subject, EmailVerificationPurpose purpose) {
		String namespace = purpose.getNamespace();

		String jsonVerificationStatus = redisTemplate.opsForValue().get(namespace + email);
		if (jsonVerificationStatus == null) {
			return false;
		}

		EmailVerificationStatus verificationStatus = JsonUtil.fromJson(jsonVerificationStatus,
			EmailVerificationStatus.class);
		if (!subject.equals(verificationStatus.getSubject())) {
			return false;
		}
		return verificationStatus.isVerified();
	}

	@Override
	public void delete(String email, EmailVerificationPurpose purpose) {
		String namespace = purpose.getNamespace();
		redisTemplate.delete(namespace + email);
	}

}
