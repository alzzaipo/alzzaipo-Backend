package com.alzzaipo.hexagonal.email.application.service;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.email.application.port.in.CheckEmailVerifiedQuery;
import com.alzzaipo.hexagonal.email.application.port.out.CheckEmailVerifiedPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckEmailVerifiedService implements CheckEmailVerifiedQuery {

    private final CheckEmailVerifiedPort checkEmailVerifiedPort;

    @Override
    public boolean checkEmailVerified(Email email) {
        return checkEmailVerifiedPort.checkEmailVerified(email);
    }
}
