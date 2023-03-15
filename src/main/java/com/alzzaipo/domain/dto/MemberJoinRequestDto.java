package com.alzzaipo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberJoinRequestDto {

    String accountId;
    String accountPassword;
    String email;
    String nickname;
}
