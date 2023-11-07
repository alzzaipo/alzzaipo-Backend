package com.alzzaipo.hexagonal.notification.adapter.out.persistence.email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NewEmailNotificationRepository extends JpaRepository<EmailNotificationJpaEntity, Long> {

    @Query("SELECT e FROM EmailNotificationJpaEntity e WHERE e.memberJpaEntity.uid = :memberUID")
    Optional<EmailNotificationJpaEntity> findByMemberUID(@Param("memberUID") Long memberUID);
}
