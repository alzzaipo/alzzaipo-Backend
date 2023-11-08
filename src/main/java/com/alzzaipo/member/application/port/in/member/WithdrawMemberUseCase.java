package com.alzzaipo.member.application.port.in.member;

import com.alzzaipo.common.Uid;

public interface WithdrawMemberUseCase {

    void withdrawMember(Uid memberUID);
}
