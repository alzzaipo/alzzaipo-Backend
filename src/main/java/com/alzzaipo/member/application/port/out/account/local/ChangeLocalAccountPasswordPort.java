package com.alzzaipo.member.application.port.out.account.local;

import com.alzzaipo.common.Id;

public interface ChangeLocalAccountPasswordPort {

    void changePassword(Id memberId, String password);
}
