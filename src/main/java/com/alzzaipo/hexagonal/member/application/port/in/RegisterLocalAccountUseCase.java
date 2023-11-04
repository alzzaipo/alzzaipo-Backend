package com.alzzaipo.hexagonal.member.application.port.in;

import com.alzzaipo.hexagonal.member.application.port.in.dto.RegisterLocalAccountCommand;

public interface RegisterLocalAccountUseCase {

    void registerLocalAccount(RegisterLocalAccountCommand command);
}
