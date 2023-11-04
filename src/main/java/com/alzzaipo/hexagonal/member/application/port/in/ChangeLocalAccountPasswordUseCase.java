package com.alzzaipo.hexagonal.member.application.port.in;

import com.alzzaipo.hexagonal.member.application.port.in.dto.ChangeLocalAccountPasswordCommand;

public interface ChangeLocalAccountPasswordUseCase {

    boolean changeLocalAccountPassword(ChangeLocalAccountPasswordCommand command);
}
