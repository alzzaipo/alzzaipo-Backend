package com.alzzaipo.hexagonal.member.adapter.out.persistence.account.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NewLocalAccountRepository extends JpaRepository<LocalAccountJpaEntity, Long> {

    @Query("SELECT l FROM LocalAccountJpaEntity l WHERE l.accountId = :accountId")
    Optional<LocalAccountJpaEntity> findByAccountId(@Param("accountId") String accountId);

    @Query("SELECT l FROM LocalAccountJpaEntity l WHERE l.email = :email")
    Optional<LocalAccountJpaEntity> findByEmail(@Param("email") String email);

    @Query("SELECT l FROM LocalAccountJpaEntity l WHERE l.memberJpaEntity.uid = :memberUID")
    Optional<LocalAccountJpaEntity> findByMemberUID(@Param("memberUID") Long memberUID);
}
