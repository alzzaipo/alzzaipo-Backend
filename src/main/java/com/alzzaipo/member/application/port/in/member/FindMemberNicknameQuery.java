package com.alzzaipo.member.application.port.in.member;

import com.alzzaipo.common.Uid;

public interface FindMemberNicknameQuery {

    String findMemberNickname(Uid memberUID);
}
