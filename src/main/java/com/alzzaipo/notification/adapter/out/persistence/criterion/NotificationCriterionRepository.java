package com.alzzaipo.notification.adapter.out.persistence.criterion;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationCriterionRepository extends JpaRepository<NotificationCriterionJpaEntity, Long> {

	List<NotificationCriterionJpaEntity> findByMemberJpaEntityId(long memberId);

	int countByMemberJpaEntityId(long memberId);

	@Query("SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END "
		+ "FROM NotificationCriterionJpaEntity n "
		+ "WHERE n.id = :notificationCriterionId AND n.memberJpaEntity.id = :memberId")
	boolean checkOwnership(@Param("memberId") long memberId, @Param("notificationCriterionId") long notificationCriterionId);
}
