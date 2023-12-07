package com.alzzaipo.member.application.port.out.member;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.domain.member.Member;

public interface FindMemberPort {

	Member findMember(Uid uid) throws CustomException;
}
