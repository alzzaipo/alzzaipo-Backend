package com.alzzaipo.member.application.port.out.account.local;

public interface VerifyLocalAccountPasswordPort {

    boolean verifyPassword(long memberId, String password);
}
