package com.alzzaipo.email.application.port.out;

import com.alzzaipo.common.Email;

import java.util.Optional;

public interface FindEmailVerificationCodePort {

    Optional<String> findEmailVerificationCode(Email email);
}
