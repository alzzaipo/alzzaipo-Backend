package com.alzzaipo.hexagonal.member.application.port.in;

import com.alzzaipo.hexagonal.member.application.port.in.dto.AuthorizationCode;
import com.alzzaipo.hexagonal.member.application.port.in.dto.LoginResult;

public interface KakaoLoginUseCase {

    LoginResult handleLogin(AuthorizationCode authorizationCode);
}
