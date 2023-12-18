package com.alzzaipo.notification.adapter.out.persistence.email;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailNotificationRepository extends JpaRepository<EmailNotificationJpaEntity, Long> {

	Optional<EmailNotificationJpaEntity> findByMemberJpaEntityUid(Long memberUID);

	boolean existsByEmail(String email);

	boolean existsByMemberJpaEntityUid(Long memberId);
}
