package com.alzzaipo.hexagonal.member.application.service.account.local;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.member.application.port.in.CheckLocalAccountEmailAvailabilityQuery;
import com.alzzaipo.hexagonal.member.application.port.out.FindLocalAccountByEmailPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckLocalAccountEmailAvailabilityService implements CheckLocalAccountEmailAvailabilityQuery {

    private final FindLocalAccountByEmailPort findLocalAccountByEmailPort;

    @Override
    public boolean checkLocalAccountEmailAvailability(Email email) {
        return findLocalAccountByEmailPort
                .findLocalAccountByEmailPort(email)
                .isEmpty();
    }
}
