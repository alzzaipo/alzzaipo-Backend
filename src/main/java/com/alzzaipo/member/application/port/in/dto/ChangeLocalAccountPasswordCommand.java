package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.Uid;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;
import lombok.Getter;

@Getter
public class ChangeLocalAccountPasswordCommand {

    private final Uid memberUID;
    private final LocalAccountPassword currentPassword;
    private final LocalAccountPassword newPassword;

    public ChangeLocalAccountPasswordCommand(Uid memberUID, String currentPassword, String newPassword) {
        this.memberUID = memberUID;
        this.currentPassword = new LocalAccountPassword(currentPassword);
        this.newPassword = new LocalAccountPassword(newPassword);
    }
}
