package com.alzzaipo.hexagonal.member.application.port.in;

import com.alzzaipo.hexagonal.common.Email;

public interface CheckLocalAccountEmailAvailabilityQuery {

    boolean checkLocalAccountEmailAvailability(Email email);
}
