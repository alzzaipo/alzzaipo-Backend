package com.alzzaipo.member.application.port.in.account.local;

import com.alzzaipo.member.application.port.in.dto.ChangeLocalAccountPasswordCommand;

public interface ChangeLocalAccountPasswordUseCase {

    boolean changeLocalAccountPassword(ChangeLocalAccountPasswordCommand command);
}
