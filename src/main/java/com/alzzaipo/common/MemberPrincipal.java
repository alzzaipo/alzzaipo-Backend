package com.alzzaipo.common;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class MemberPrincipal implements Serializable {

    private final Uid memberUID;
    private final LoginType currentLoginType;

    public MemberPrincipal(Uid memberUID, LoginType currentLoginType) {
        this.memberUID = memberUID;
        this.currentLoginType = currentLoginType;
    }
}
