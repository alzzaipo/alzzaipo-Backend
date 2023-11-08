package com.alzzaipo.member.application.port.in.oauth;

import com.alzzaipo.common.Uid;
import com.alzzaipo.member.application.port.in.dto.AuthorizationCode;

public interface LinkKakaoAccountUseCase {

    boolean linkKakaoAccountUseCase(Uid memberUID, AuthorizationCode authorizationCode);
}
