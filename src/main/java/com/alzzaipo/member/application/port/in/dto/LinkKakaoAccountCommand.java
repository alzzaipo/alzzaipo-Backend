package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LinkKakaoAccountCommand {

    private final Id memberId;
    private final AuthorizationCode authorizationCode;
}
