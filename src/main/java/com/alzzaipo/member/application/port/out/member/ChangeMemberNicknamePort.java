package com.alzzaipo.member.application.port.out.member;

import com.alzzaipo.common.Id;

public interface ChangeMemberNicknamePort {

    void changeMemberNickname(Id memberId, String nickname);
}
