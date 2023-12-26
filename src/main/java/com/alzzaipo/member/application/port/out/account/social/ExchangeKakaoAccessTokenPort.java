package com.alzzaipo.member.application.port.out.account.social;

import com.alzzaipo.member.application.port.in.dto.AuthorizationCode;
import com.alzzaipo.member.application.port.out.dto.AccessToken;

public interface ExchangeKakaoAccessTokenPort {

    AccessToken exchangeKakaoAccessToken(AuthorizationCode authorizationCode);
}
