package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.Id;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;
import lombok.Getter;

@Getter
public class ChangeLocalAccountPasswordCommand {

    private final Id memberId;
    private final LocalAccountPassword currentPassword;
    private final LocalAccountPassword newPassword;

    public ChangeLocalAccountPasswordCommand(Id memberId, String currentPassword, String newPassword) {
        this.memberId = memberId;
        this.currentPassword = new LocalAccountPassword(currentPassword);
        this.newPassword = new LocalAccountPassword(newPassword);
    }
}
