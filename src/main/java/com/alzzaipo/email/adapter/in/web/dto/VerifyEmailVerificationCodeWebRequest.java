package com.alzzaipo.email.adapter.in.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VerifyEmailVerificationCodeWebRequest {
    private String email;
    private String verificationCode;
}
