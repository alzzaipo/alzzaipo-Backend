package com.alzzaipo.domain.account.social;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    @Query("SELECT s FROM SocialAccount s WHERE s.email = ?1 AND s.socialCode = ?2")
    Optional<SocialAccount> findByEmailAndSocialCode(String email, SocialCode socialCode);
}
