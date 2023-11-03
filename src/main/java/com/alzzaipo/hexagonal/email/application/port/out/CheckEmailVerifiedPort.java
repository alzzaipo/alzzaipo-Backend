package com.alzzaipo.hexagonal.email.application.port.out;

import com.alzzaipo.hexagonal.common.Email;

public interface CheckEmailVerifiedPort {

    boolean checkEmailVerified(Email email);
}
