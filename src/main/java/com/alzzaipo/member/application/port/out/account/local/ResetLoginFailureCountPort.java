package com.alzzaipo.member.application.port.out.account.local;

public interface ResetLoginFailureCountPort {

    void reset(String clientIP);
}
