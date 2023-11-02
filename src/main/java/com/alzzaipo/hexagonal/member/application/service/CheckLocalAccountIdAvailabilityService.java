package com.alzzaipo.hexagonal.member.application.service;

import com.alzzaipo.hexagonal.member.application.port.in.CheckLocalAccountIdAvailabilityUseCase;
import com.alzzaipo.hexagonal.member.application.port.out.FindLocalAccountByAccountIdPort;
import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckLocalAccountIdAvailabilityService implements CheckLocalAccountIdAvailabilityUseCase {

    private final FindLocalAccountByAccountIdPort findLocalAccountByAccountIdPort;

    @Override
    public boolean checkLocalAccountIdAvailability(LocalAccountId localAccountId) {
        return findLocalAccountByAccountIdPort
                .findLocalAccountByAccountId(localAccountId)
                .isEmpty();
    }
}
