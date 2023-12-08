package com.alzzaipo.notification.adapter.out.persistence.criterion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationCriterionRepository extends JpaRepository<NotificationCriterionJpaEntity, Long> {

    @Query("SELECT n FROM NotificationCriterionJpaEntity n WHERE n.memberJpaEntity.uid = :memberUID")
    List<NotificationCriterionJpaEntity> findByMemberUID(@Param("memberUID") Long memberUID);

    @Query("SELECT n FROM NotificationCriterionJpaEntity n WHERE n.notificationCriterionUID = :uid")
    Optional<NotificationCriterionJpaEntity> findByNotificationCriterionUID(@Param("uid") Long notificationCriterionUID);
}
