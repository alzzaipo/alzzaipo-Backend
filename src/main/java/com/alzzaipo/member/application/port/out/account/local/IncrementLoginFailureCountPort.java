package com.alzzaipo.member.application.port.out.account.local;

public interface IncrementLoginFailureCountPort {

    void increment(String clientIpAddress);
}
