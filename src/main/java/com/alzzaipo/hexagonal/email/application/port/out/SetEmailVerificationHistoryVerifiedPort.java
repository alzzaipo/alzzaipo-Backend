package com.alzzaipo.hexagonal.email.application.port.out;

import com.alzzaipo.hexagonal.common.Email;

public interface SetEmailVerificationHistoryVerifiedPort {

    void setEmailVerificationHistoryVerified(Email email);
}
