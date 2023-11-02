package com.alzzaipo.hexagonal.member.application.port.in;

import com.alzzaipo.hexagonal.email.domain.Email;

public interface CheckLocalAccountEmailAvailabilityQuery {

    boolean checkLocalAccountEmailAvailability(Email email);
}
