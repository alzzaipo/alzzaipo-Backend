package com.alzzaipo.dto.account.local;

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
