package com.alzzaipo.notification.adapter.out.persistence.criterion;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationCriterionRepository extends JpaRepository<NotificationCriterionJpaEntity, Long> {

	List<NotificationCriterionJpaEntity> findByMemberJpaEntityUid(@Param("memberUID") Long memberUID);

	int countByMemberJpaEntityUid(Long memberId);

	@Query("SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END "
		+ "FROM NotificationCriterionJpaEntity n "
		+ "WHERE n.notificationCriterionUID = :notificationCriterionId AND n.memberJpaEntity.uid = :memberId")
	boolean checkOwnership(@Param("memberId") Long memberId, @Param("notificationCriterionId") Long notificationCriterionId);
}
