package com.alzzaipo.hexagonal.member.application.port.in;

public interface ChangeLocalAccountPasswordUseCase {

    boolean changeLocalAccountPassword(ChangeLocalAccountPasswordCommand command);
}
