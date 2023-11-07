package com.alzzaipo.email.application.port.out;

import com.alzzaipo.common.Email;
import com.alzzaipo.email.domain.EmailVerificationCode;

public interface SaveEmailVerificationHistoryPort {

    void saveEmailVerificationHistory(Email email, EmailVerificationCode emailVerificationCode);
}
