package com.alzzaipo.hexagonal.member.application.port.in.account.local;

import com.alzzaipo.hexagonal.member.application.port.in.dto.LocalLoginCommand;
import com.alzzaipo.hexagonal.member.application.port.in.dto.LoginResult;

public interface LocalLoginUseCase {

    LoginResult handleLocalLogin(LocalLoginCommand command);
}
