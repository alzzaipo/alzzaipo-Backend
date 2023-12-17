package com.alzzaipo.common.email.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailVerificationStatus {

	private String subject;
	private boolean verified;

	public EmailVerificationStatus(String subject, boolean verified) {
		this.subject = subject;
		this.verified = verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}
}
