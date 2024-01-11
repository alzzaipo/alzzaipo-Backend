package com.alzzaipo.member.application.port.out.account.local;

public interface CheckLoginFailureCountLimitReachedPort {

    boolean checkLimitReached(String clientIP);
}
