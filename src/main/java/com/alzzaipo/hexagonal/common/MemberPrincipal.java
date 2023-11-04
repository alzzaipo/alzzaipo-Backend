package com.alzzaipo.hexagonal.common;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class MemberPrincipal implements Serializable {
    private final Uid memberId;
    private final LoginType currentLoginType;

    public MemberPrincipal(Uid memberId, LoginType currentLoginType) {
        this.memberId = memberId;
        this.currentLoginType = currentLoginType;
    }
}
