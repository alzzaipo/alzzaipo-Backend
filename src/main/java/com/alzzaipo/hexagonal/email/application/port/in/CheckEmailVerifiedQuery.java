package com.alzzaipo.hexagonal.email.application.port.in;

import com.alzzaipo.hexagonal.common.Email;

public interface CheckEmailVerifiedQuery {

    boolean checkEmailVerified(Email email);
}
