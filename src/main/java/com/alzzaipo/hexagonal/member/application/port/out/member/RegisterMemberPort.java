package com.alzzaipo.hexagonal.member.application.port.out.member;

import com.alzzaipo.hexagonal.member.domain.member.Member;

public interface RegisterMemberPort {

    void registerMember(Member member);
}
