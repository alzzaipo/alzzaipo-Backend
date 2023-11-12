package com.alzzaipo.member.application.port.in.oauth;

import com.alzzaipo.common.Uid;
import com.alzzaipo.member.application.port.in.dto.AuthorizationCode;

public interface LinkKakaoAccountUseCase {

    void linkKakaoAccount(Uid memberUID, AuthorizationCode authorizationCode);
}
