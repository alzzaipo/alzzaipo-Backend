package com.alzzaipo.hexagonal.email.application.port.out;

import com.alzzaipo.hexagonal.email.domain.Email;

public interface SetEmailVerificationHistoryVerifiedPort {

    void setEmailVerificationHistoryVerified(Email email);
}
