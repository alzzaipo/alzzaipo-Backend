package com.alzzaipo.hexagonal.member.application.port.in;

import com.alzzaipo.hexagonal.member.application.port.in.dto.LocalLoginCommand;
import com.alzzaipo.hexagonal.member.application.port.in.dto.LocalLoginResult;

public interface LocalLoginUseCase {

    LocalLoginResult handleLocalLogin(LocalLoginCommand command);
}
