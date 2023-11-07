package com.alzzaipo.email.application.port.out;

import com.alzzaipo.common.Email;

public interface SetEmailVerificationHistoryVerifiedPort {

    void setEmailVerificationHistoryVerified(Email email);
}
