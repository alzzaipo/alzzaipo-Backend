package com.alzzaipo.hexagonal.email.application.port.in;

import com.alzzaipo.hexagonal.common.Email;

public interface SendCustomEmail {

    void sendCustomEmail(Email to, String subject, String text);
}
