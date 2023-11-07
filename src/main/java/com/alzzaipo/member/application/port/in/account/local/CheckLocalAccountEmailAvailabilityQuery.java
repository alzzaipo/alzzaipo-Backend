package com.alzzaipo.member.application.port.in.account.local;

import com.alzzaipo.common.Email;

public interface CheckLocalAccountEmailAvailabilityQuery {

    boolean checkLocalAccountEmailAvailability(Email email);
}
