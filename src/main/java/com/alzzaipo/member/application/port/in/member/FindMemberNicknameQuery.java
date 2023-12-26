package com.alzzaipo.member.application.port.in.member;

import com.alzzaipo.common.Id;

public interface FindMemberNicknameQuery {

    String findMemberNickname(Id memberId);
}
