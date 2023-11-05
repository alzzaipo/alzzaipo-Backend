package com.alzzaipo.hexagonal.member.application.port.out.oauth;

import com.alzzaipo.hexagonal.member.application.port.in.dto.AuthorizationCode;
import com.alzzaipo.hexagonal.member.application.port.out.dto.AccessToken;

public interface ExchangeKakaoAccessTokenPort {

    AccessToken exchangeKakaoAccessToken(AuthorizationCode authorizationCode);
}
