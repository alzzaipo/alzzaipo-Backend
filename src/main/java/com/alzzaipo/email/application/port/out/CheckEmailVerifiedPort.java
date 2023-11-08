package com.alzzaipo.email.application.port.out;

import com.alzzaipo.common.Email;

public interface CheckEmailVerifiedPort {

    boolean checkEmailVerified(Email email);
}
