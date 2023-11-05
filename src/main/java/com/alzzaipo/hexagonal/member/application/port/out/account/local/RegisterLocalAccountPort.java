package com.alzzaipo.hexagonal.member.application.port.out.account.local;

import com.alzzaipo.hexagonal.member.application.port.out.dto.SecureLocalAccount;

public interface RegisterLocalAccountPort {

    void registerLocalAccountPort(SecureLocalAccount localAccount);
}
