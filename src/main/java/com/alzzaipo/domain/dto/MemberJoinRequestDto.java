package com.alzzaipo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberJoinRequestDto {

    private String accountId;
    private String accountPassword;
    private String email;
    private String nickname;
}