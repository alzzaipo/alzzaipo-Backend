package com.alzzaipo.notification.adapter.out.persistence.email;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailNotificationRepository extends JpaRepository<EmailNotificationJpaEntity, Long> {

	Optional<EmailNotificationJpaEntity> findByMemberJpaEntityId(long memberId);

	boolean existsByEmail(String email);

	boolean existsByMemberJpaEntityId(long memberId);
}
