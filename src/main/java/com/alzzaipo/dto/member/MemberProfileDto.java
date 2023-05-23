package com.alzzaipo.dto.member;

import com.alzzaipo.enums.LoginType;
import com.alzzaipo.enums.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MemberProfileDto {
    private MemberType memberType;
    private String accountId;
    private String nickname;
    private String email;
    private List<LoginType> linkedLoginType;
    private LoginType currentLoginType;
}
