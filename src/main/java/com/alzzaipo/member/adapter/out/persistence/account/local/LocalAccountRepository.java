package com.alzzaipo.member.adapter.out.persistence.account.local;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalAccountRepository extends JpaRepository<LocalAccountJpaEntity, Long> {

	Optional<LocalAccountJpaEntity> findByAccountId(String accountId);

	Optional<LocalAccountJpaEntity> findByMemberJpaEntityUid(Long memberUID);

	boolean existsByAccountId(String accountId);

	boolean existsByEmail(String email);
}
