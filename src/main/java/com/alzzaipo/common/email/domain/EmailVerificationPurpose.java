package com.alzzaipo.common.email.domain;

import lombok.Getter;

@Getter
public enum EmailVerificationPurpose {
	SIGN_UP("sign-up:"), UPDATE("update-email:"), NOTIFICATION("notification:");

	private final String namespace;

	EmailVerificationPurpose(String namespace) {
		this.namespace = namespace;
	}
}
