package com.alzzaipo.hexagonal.member.application.port.in.oauth;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.application.port.in.dto.AuthorizationCode;

public interface LinkKakaoAccountUseCase {

    boolean linkKakaoAccountUseCase(Uid memberUID, AuthorizationCode authorizationCode);
}
