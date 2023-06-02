package com.alzzaipo.domain.notification.criteria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationCriteriaRepository extends JpaRepository<NotificationCriteria, Long> {

    @Query("SELECT n FROM NotificationCriteria n where n.member.id = ?1")
    List<NotificationCriteria> findByMemberId(Long id);
}
