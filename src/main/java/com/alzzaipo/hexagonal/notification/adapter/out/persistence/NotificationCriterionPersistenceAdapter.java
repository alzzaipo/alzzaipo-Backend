package com.alzzaipo.hexagonal.notification.adapter.out.persistence;

import com.alzzaipo.hexagonal.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.member.NewMemberRepository;
import com.alzzaipo.hexagonal.notification.application.port.out.RegisterNotificationCriterionPort;
import com.alzzaipo.hexagonal.notification.domain.NotificationCriterion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class NotificationCriterionPersistenceAdapter implements RegisterNotificationCriterionPort {

    private final NewNotificationCriterionRepository notificationCriteriaRepository;
    private final NewMemberRepository memberRepository;

    @Override
    public void registerNotificationCriterion(NotificationCriterion notificationCriterion) {
        MemberJpaEntity memberJpaEntity = memberRepository.findByUid(notificationCriterion.getMemberUID().get())
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));

        NotificationCriterionJpaEntity entity = toJpaEntity(notificationCriterion, memberJpaEntity);
        notificationCriteriaRepository.save(entity);
    }

    private NotificationCriterionJpaEntity toJpaEntity(NotificationCriterion notificationCriterion, MemberJpaEntity memberJpaEntity) {
        return new NotificationCriterionJpaEntity(
                notificationCriterion.getMinCompetitionRate(),
                notificationCriterion.getMinLockupRate(),
                memberJpaEntity);
    }
}
