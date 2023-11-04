package com.alzzaipo.hexagonal.member.adapter.out.persistence.SocialAccount;

import com.alzzaipo.hexagonal.common.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NewSocialAccountRepository extends JpaRepository<SocialAccountJpaEntity, Long> {

    @Query("SELECT s FROM SocialAccountJpaEntity s WHERE s.loginType = :loginType AND s.email = :email")
    Optional<SocialAccountJpaEntity> findByLoginTypeAndEmail(
            @Param("loginType") LoginType loginType,
            @Param("email") String email);
}
