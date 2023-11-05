package com.alzzaipo.hexagonal.member.adapter.out.persistence.SocialAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NewSocialAccountRepository extends JpaRepository<SocialAccountJpaEntity, Long> {

    @Query("SELECT s FROM SocialAccountJpaEntity s WHERE s.loginType = :loginType AND s.email = :email")
    Optional<SocialAccountJpaEntity> findByLoginTypeAndEmail(
            @Param("loginType") String loginType,
            @Param("email") String email);

    @Query("SELECT s FROM SocialAccountJpaEntity s WHERE s.memberJpaEntity.uid = :memberUID")
    List<SocialAccountJpaEntity> findByMemberUID(@Param("memberUID") Long memberUID);
}
