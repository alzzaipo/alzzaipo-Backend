package com.alzzaipo.config;

import com.alzzaipo.enums.LoginType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPrincipal {
    private Long memberId;
    private LoginType currentLoginType;
}