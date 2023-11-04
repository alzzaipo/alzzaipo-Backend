package com.alzzaipo.hexagonal.member.application.port.in;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountPassword;
import lombok.Getter;

@Getter
public class ChangeLocalAccountPasswordCommand {

    private final Uid memberUID;
    private final LocalAccountPassword newPassword;

    public ChangeLocalAccountPasswordCommand(Uid memberUID, LocalAccountPassword newPassword) {
        this.memberUID = memberUID;
        this.newPassword = newPassword;
    }
}
