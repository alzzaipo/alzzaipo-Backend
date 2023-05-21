package com.alzzaipo.domain.account.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LocalAccountRepository extends JpaRepository<LocalAccount, Long> {
    @Query
    Optional<LocalAccount> findByEmail(String email);

    @Query
    Optional<LocalAccount> findByAccountId(String accountId);

    @Query("SELECT l FROM LocalAccount l WHERE l.member.id = ?1")
    Optional<LocalAccount> findByMemberId(Long memberId);
}
