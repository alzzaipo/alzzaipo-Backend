package com.alzzaipo.domain.account.social;

import com.alzzaipo.enums.SocialCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    @Query("SELECT s FROM SocialAccount s WHERE s.email = ?1 AND s.socialCode = ?2")
    Optional<SocialAccount> findByEmailAndSocialCode(String email, SocialCode socialCode);

    @Query("SELECT s.socialCode FROM SocialAccount s WHERE s.member.id = ?1")
    List<SocialCode> findSocialLoginTypes(Long memberId);
}
