package com.alzzaipo.hexagonal.member.application.port.out;

import com.alzzaipo.hexagonal.common.Uid;

import java.util.Optional;

public interface FindLocalAccountByMemberUidPort {

    Optional<SecureLocalAccount> findLocalAccountByMemberUid(Uid memberUID);
}
