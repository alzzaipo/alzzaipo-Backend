package com.alzzaipo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailVerificationRequestDto {
    private String email;
    private String authCode;
}