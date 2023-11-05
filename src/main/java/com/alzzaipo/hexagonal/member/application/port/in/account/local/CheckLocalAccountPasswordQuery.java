package com.alzzaipo.hexagonal.member.application.port.in.account.local;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.domain.account.local.LocalAccountPassword;

public interface CheckLocalAccountPasswordQuery {

    boolean checkLocalAccountPassword(Uid memberUID, LocalAccountPassword localAccountPassword);
}
