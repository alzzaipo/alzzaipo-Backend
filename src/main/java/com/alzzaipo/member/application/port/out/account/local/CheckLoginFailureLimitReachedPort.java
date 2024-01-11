package com.alzzaipo.member.application.port.out.account.local;

public interface CheckLoginFailureLimitReachedPort {

    boolean checkLimitReached(String clientIP);
}
