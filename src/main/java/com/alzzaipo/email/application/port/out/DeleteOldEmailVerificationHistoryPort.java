package com.alzzaipo.email.application.port.out;

import com.alzzaipo.common.Email;

public interface DeleteOldEmailVerificationHistoryPort {

    void deleteOldEmailVerificationHistory(Email email);
}
