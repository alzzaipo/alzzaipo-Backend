package com.alzzaipo.hexagonal.member.application.port.in;

public interface LocalLoginUseCase {

    LocalLoginResult handleLocalLogin(LocalLoginCommand command);
}
