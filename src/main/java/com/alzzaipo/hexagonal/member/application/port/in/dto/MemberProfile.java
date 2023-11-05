package com.alzzaipo.hexagonal.member.application.port.in.dto;

import com.alzzaipo.hexagonal.common.LoginType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MemberProfile {

    private final MemberType memberType;
    private final String accountId;
    private final String nickname;
    private final String email;
    private final List<LoginType> linkedLoginTypes;
    private final LoginType currentLoginType;
}
