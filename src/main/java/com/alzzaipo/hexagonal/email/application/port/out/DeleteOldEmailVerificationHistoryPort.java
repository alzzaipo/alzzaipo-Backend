package com.alzzaipo.hexagonal.email.application.port.out;

import com.alzzaipo.hexagonal.common.Email;

public interface DeleteOldEmailVerificationHistoryPort {

    void deleteOldEmailVerificationHistory(Email email);
}
