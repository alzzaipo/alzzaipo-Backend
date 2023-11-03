package com.alzzaipo.hexagonal.email.application.port.out;

import com.alzzaipo.hexagonal.common.Email;

import java.util.Optional;

public interface FindEmailVerificationCodePort {

    Optional<String> findEmailVerificationCode(Email email);
}
