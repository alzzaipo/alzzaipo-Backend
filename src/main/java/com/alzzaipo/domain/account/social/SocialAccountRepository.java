package com.alzzaipo.domain.account.social;

import com.alzzaipo.enums.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    @Query("SELECT s FROM SocialAccount s WHERE s.email = ?1 AND s.loginType = ?2")
    Optional<SocialAccount> findByEmailAndLoginType(String email, LoginType loginType);

    @Query("SELECT s.loginType FROM SocialAccount s WHERE s.member.id = ?1")
    List<LoginType> findLinkedSocialLoginTypes(Long memberId);

    @Query("SELECT s FROM SocialAccount s WHERE s.member.id = ?1 AND s.loginType = ?2")
    Optional<SocialAccount> findByMemberIdAndLoginType(Long memberId, LoginType loginType);
}
