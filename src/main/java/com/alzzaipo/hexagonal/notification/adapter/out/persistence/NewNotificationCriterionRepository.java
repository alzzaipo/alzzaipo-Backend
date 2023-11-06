package com.alzzaipo.hexagonal.notification.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewNotificationCriterionRepository extends JpaRepository<NotificationCriterionJpaEntity, Long> {

    @Query("SELECT n FROM NotificationCriterionJpaEntity n WHERE n.memberJpaEntity.uid = :memberUID")
    List<NotificationCriterionJpaEntity> findByMemberUID(@Param("memberUID") Long memberUID);
}
