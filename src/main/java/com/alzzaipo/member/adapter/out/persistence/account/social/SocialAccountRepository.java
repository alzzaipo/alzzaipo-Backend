package com.alzzaipo.member.adapter.out.persistence.account.social;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccountJpaEntity, Long> {

    @Query("SELECT s FROM SocialAccountJpaEntity s WHERE s.loginType = :loginType AND s.email = :email")
    Optional<SocialAccountJpaEntity> findByLoginTypeAndEmail(
            @Param("loginType") String loginType,
            @Param("email") String email);

    @Query("SELECT s FROM SocialAccountJpaEntity s WHERE s.memberJpaEntity.uid = :memberUID")
    List<SocialAccountJpaEntity> findByMemberUID(@Param("memberUID") Long memberUID);

    @Query("SELECT s FROM SocialAccountJpaEntity s WHERE s.memberJpaEntity.uid = :memberUID AND s.loginType = :loginType")
    Optional<SocialAccountJpaEntity> findByLoginType(
            @Param("memberUID") Long memberUID,
            @Param("loginType") String loginType);
}
