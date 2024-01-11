package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.Id;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeLocalAccountPasswordCommand {

    private final Id memberId;
    private final LocalAccountPassword currentPassword;
    private final LocalAccountPassword newPassword;
}
