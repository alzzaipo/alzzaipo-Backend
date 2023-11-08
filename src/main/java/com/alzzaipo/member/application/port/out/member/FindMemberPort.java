package com.alzzaipo.member.application.port.out.member;

import com.alzzaipo.common.Uid;
import com.alzzaipo.member.domain.member.Member;

import java.util.Optional;

public interface FindMemberPort {

    Optional<Member> findMember(Uid uid);
}
