package com.alzzaipo.member.application.port.out.account.local;

import com.alzzaipo.common.Uid;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;

import java.util.Optional;

public interface FindLocalAccountByMemberUidPort {

    Optional<SecureLocalAccount> findByMemberId(Uid memberUID);
}
