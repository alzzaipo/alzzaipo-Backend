package com.alzzaipo.email.application.port.in;

import com.alzzaipo.common.Email;

public interface SendCustomEmail {

    void sendCustomEmail(Email to, String subject, String text);
}
