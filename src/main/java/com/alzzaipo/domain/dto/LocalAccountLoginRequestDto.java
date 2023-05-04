package com.alzzaipo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LocalAccountLoginRequestDto {

    private String accountId;
    private String accountPassword;
}
