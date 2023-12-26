package com.alzzaipo.member.application.port.out.account.local;

import com.alzzaipo.common.Id;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;

import java.util.Optional;

public interface FindLocalAccountByMemberIdPort {

    Optional<SecureLocalAccount> findByMemberId(Id memberId);
}
