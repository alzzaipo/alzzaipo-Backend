package com.alzzaipo.member.application.port.out.member;

import com.alzzaipo.common.Uid;

public interface ChangeMemberNicknamePort {

    void changeMemberNickname(Uid memberUID, String nickname);
}
