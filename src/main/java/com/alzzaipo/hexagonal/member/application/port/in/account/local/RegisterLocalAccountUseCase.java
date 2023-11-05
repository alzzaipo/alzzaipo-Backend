package com.alzzaipo.hexagonal.member.application.port.in.account.local;

import com.alzzaipo.hexagonal.member.application.port.in.dto.RegisterLocalAccountCommand;

public interface RegisterLocalAccountUseCase {

    void registerLocalAccount(RegisterLocalAccountCommand command);
}
