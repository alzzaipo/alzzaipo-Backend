package com.alzzaipo.hexagonal.member.application.service.account.local;

import com.alzzaipo.hexagonal.member.application.port.in.account.local.CheckLocalAccountIdAvailabilityQuery;
import com.alzzaipo.hexagonal.member.application.port.out.account.local.FindLocalAccountByAccountIdPort;
import com.alzzaipo.hexagonal.member.domain.account.local.LocalAccountId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckLocalAccountIdAvailabilityService implements CheckLocalAccountIdAvailabilityQuery {

    private final FindLocalAccountByAccountIdPort findLocalAccountByAccountIdPort;

    @Override
    public boolean checkLocalAccountIdAvailability(LocalAccountId localAccountId) {
        return findLocalAccountByAccountIdPort
                .findLocalAccountByAccountId(localAccountId)
                .isEmpty();
    }
}
