package com.alzzaipo.member.application.port.in.account.social;

import com.alzzaipo.common.Id;
import com.alzzaipo.member.application.port.in.dto.AuthorizationCode;

public interface LinkKakaoAccountUseCase {

    void linkKakaoAccount(Id memberId, AuthorizationCode authorizationCode);
}
