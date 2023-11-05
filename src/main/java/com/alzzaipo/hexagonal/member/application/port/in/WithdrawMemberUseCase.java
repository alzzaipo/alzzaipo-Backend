package com.alzzaipo.hexagonal.member.application.port.in;

import com.alzzaipo.hexagonal.common.Uid;

public interface WithdrawMemberUseCase {

    void withdrawMember(Uid memberUID);
}
