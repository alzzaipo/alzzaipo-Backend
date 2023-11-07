package com.alzzaipo.member.application.service.account.local;

import com.alzzaipo.common.Email;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountEmailAvailabilityQuery;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByEmailPort;
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
