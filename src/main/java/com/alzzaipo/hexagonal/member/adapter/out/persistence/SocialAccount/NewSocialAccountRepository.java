package com.alzzaipo.hexagonal.member.adapter.out.persistence.SocialAccount;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NewSocialAccountRepository extends JpaRepository<SocialAccountJpaEntity, Long> {
}
