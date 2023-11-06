package com.alzzaipo.hexagonal.notification.adapter.out.persistence;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.member.NewMemberRepository;
import com.alzzaipo.hexagonal.notification.application.port.out.FindMemberNotificationCriteriaPort;
import com.alzzaipo.hexagonal.notification.application.port.out.RegisterNotificationCriterionPort;
import com.alzzaipo.hexagonal.notification.domain.NotificationCriterion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class NotificationCriterionPersistenceAdapter implements
        RegisterNotificationCriterionPort,
        FindMemberNotificationCriteriaPort {

    private final NewNotificationCriterionRepository notificationCriteriaRepository;
    private final NewMemberRepository memberRepository;

    @Override
    public void registerNotificationCriterion(NotificationCriterion notificationCriterion) {
        MemberJpaEntity memberJpaEntity = memberRepository.findByUid(notificationCriterion.getMemberUID().get())
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));

        NotificationCriterionJpaEntity entity = toJpaEntity(notificationCriterion, memberJpaEntity);
        notificationCriteriaRepository.save(entity);
    }

    @Override
    public List<NotificationCriterion> findMemberNotificationCriteria(Uid memberUID) {
        return notificationCriteriaRepository.findByMemberUID(memberUID.get())
                .stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    private NotificationCriterionJpaEntity toJpaEntity(NotificationCriterion domainEntity, MemberJpaEntity memberJpaEntity) {
        return new NotificationCriterionJpaEntity(
                domainEntity.getNotificationCriterionUID().get(),
                domainEntity.getMinCompetitionRate(),
                domainEntity.getMinLockupRate(),
                memberJpaEntity);
    }

    private NotificationCriterion toDomainEntity(NotificationCriterionJpaEntity jpaEntity) {
        return new NotificationCriterion(
                new Uid(jpaEntity.getNotificationCriterionUID()),
                new Uid(jpaEntity.getMemberJpaEntity().getUid()),
                jpaEntity.getMinCompetitionRate(),
                jpaEntity.getMinLockupRate());
    }

}