package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.member.domain.account.local.LocalAccountId;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocalLoginCommand {

    private final LocalAccountId localAccountId;
    private final LocalAccountPassword localAccountPassword;
    private final String captchaResponse;
    private final String clientIP;
}
