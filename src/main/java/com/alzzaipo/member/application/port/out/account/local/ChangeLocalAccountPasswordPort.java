package com.alzzaipo.member.application.port.out.account.local;

public interface ChangeLocalAccountPasswordPort {

    void changePassword(long memberId, String password);
}
