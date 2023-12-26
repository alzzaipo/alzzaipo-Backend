package com.alzzaipo.member.adapter.out.persistence.account.social;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccountJpaEntity, Long> {

    Optional<SocialAccountJpaEntity> findByLoginTypeAndEmail(String loginType, String email);

    List<SocialAccountJpaEntity> findByMemberJpaEntityId(long memberId);

    Optional<SocialAccountJpaEntity> findByMemberJpaEntityIdAndLoginType(long memberId, String loginType);
}
