package com.alzzaipo.hexagonal.member.adapter.out.persistence.LocalAccount;

import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NewLocalAccountRepository extends JpaRepository<LocalAccountJpaEntity, Long> {

    @Query("SELECT l FROM LocalAccountJpaEntity l WHERE l.accountId = :accountId")
    Optional<LocalAccount> findByAccountId(@Param("accountId") String accountId);

    @Query("SELECT l FROM LocalAccountJpaEntity l WHERE l.email = :email")
    Optional<LocalAccount> findByEmail(@Param("email") String email);
}
