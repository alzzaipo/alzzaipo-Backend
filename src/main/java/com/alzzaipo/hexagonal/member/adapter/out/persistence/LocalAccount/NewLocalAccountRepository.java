package com.alzzaipo.hexagonal.member.adapter.out.persistence.LocalAccount;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NewLocalAccountRepository extends JpaRepository<LocalAccountJpaEntity, Long> {
}
