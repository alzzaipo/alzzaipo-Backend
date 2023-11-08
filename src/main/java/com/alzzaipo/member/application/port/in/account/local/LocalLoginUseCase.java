package com.alzzaipo.member.application.port.in.account.local;

import com.alzzaipo.member.application.port.in.dto.LocalLoginCommand;
import com.alzzaipo.member.application.port.in.dto.LoginResult;

public interface LocalLoginUseCase {

    LoginResult handleLocalLogin(LocalLoginCommand command);
}
