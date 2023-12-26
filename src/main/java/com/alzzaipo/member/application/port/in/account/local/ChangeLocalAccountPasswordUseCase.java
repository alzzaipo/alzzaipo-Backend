package com.alzzaipo.member.application.port.in.account.local;

import com.alzzaipo.member.application.port.in.dto.ChangeLocalAccountPasswordCommand;

public interface ChangeLocalAccountPasswordUseCase {

    boolean changePassword(ChangeLocalAccountPasswordCommand command);
}
