package com.alzzaipo.hexagonal.member.application.port.in.account.local;

import com.alzzaipo.hexagonal.common.Email;

public interface CheckLocalAccountEmailAvailabilityQuery {

    boolean checkLocalAccountEmailAvailability(Email email);
}
