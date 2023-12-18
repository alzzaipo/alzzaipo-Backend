package com.alzzaipo.common.email.port.out.smtp;

public interface SendCustomEmailPort {

    void send(String to, String subject, String text);
}
