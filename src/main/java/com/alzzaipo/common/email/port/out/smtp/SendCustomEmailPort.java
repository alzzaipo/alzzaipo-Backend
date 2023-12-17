package com.alzzaipo.common.email.port.out.smtp;

import com.alzzaipo.common.email.domain.Email;

public interface SendCustomEmailPort {

    void send(Email to, String subject, String text);
}
