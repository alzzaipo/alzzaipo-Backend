package com.alzzaipo.hexagonal.member.application.port.out.member;

import com.alzzaipo.hexagonal.common.Uid;

public interface WithdrawMemberPort {

    void withdrawMember(Uid memberUID);
}
