package com.alzzaipo.dto.account.local;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LocalAccountRegisterRequestDto {

    private String accountId;
    private String accountPassword;
    private String email;
    private String nickname;
}