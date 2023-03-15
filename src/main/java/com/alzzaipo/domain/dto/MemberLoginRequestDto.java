package com.alzzaipo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberLoginRequestDto {

    private String accountId;
    private String accountPassword;
}
