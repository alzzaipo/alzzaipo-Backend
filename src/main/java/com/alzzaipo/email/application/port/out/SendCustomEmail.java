package com.alzzaipo.email.application.port.out;

import com.alzzaipo.common.Email;

public interface SendCustomEmail {

    void sendCustomEmail(Email to, String subject, String text);
}
