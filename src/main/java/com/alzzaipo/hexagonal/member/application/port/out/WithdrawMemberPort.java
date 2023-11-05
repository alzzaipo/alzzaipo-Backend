package com.alzzaipo.hexagonal.member.application.port.out;

import com.alzzaipo.hexagonal.common.Uid;

public interface WithdrawMemberPort {

    void withdrawMember(Uid memberUID);
}
