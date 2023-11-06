package com.alzzaipo.hexagonal.notification.adapter.out.persistence;

import com.alzzaipo.hexagonal.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.member.NewMemberRepository;
import com.alzzaipo.hexagonal.notification.application.port.out.RegisterNotificationCriteriaPort;
import com.alzzaipo.hexagonal.notification.domain.NotificationCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class NotificationCriteriaPersistenceAdapter implements RegisterNotificationCriteriaPort {

    private final NewNotificationCriteriaRepository notificationCriteriaRepository;
    private final NewMemberRepository memberRepository;

    @Override
    public void registerNotificationCriteria(NotificationCriteria notificationCriteria) {
        MemberJpaEntity memberJpaEntity = memberRepository.findByUid(notificationCriteria.getMemberUID().get())
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));

        NotificationCriteriaJpaEntity entity = toJpaEntity(notificationCriteria, memberJpaEntity);
        notificationCriteriaRepository.save(entity);
    }

    private NotificationCriteriaJpaEntity toJpaEntity(NotificationCriteria notificationCriteria, MemberJpaEntity memberJpaEntity) {
        return new NotificationCriteriaJpaEntity(
                notificationCriteria.getMinCompetitionRate(),
                notificationCriteria.getMinLockupRate(),
                memberJpaEntity);
    }
}
