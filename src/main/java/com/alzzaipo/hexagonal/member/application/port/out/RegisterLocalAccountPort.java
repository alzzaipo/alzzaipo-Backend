package com.alzzaipo.hexagonal.member.application.port.out;

import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccount;

public interface RegisterLocalAccountPort {

    void registerLocalAccountPort(LocalAccount localAccount);
}
