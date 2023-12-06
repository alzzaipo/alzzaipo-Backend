package com.alzzaipo.email.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VerifyEmailVerificationCodeWebRequest {

	@Email
	private String email;

	@NotBlank
	private String verificationCode;
}
