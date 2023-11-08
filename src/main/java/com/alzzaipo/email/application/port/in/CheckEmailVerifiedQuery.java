package com.alzzaipo.email.application.port.in;

import com.alzzaipo.common.Email;

public interface CheckEmailVerifiedQuery {

    boolean checkEmailVerified(Email email);
}
