package com.alzzaipo.hexagonal.member.application.port.out;

import com.alzzaipo.hexagonal.member.application.port.out.dto.SecureLocalAccount;

public interface RegisterLocalAccountPort {

    void registerLocalAccountPort(SecureLocalAccount localAccount);
}
