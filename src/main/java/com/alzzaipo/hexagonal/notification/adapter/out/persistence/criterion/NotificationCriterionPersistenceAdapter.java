package com.alzzaipo.hexagonal.notification.adapter.out.persistence.criterion;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.member.NewMemberRepository;
import com.alzzaipo.hexagonal.notification.application.port.dto.UpdateNotificationCriterionCommand;
import com.alzzaipo.hexagonal.notification.application.port.out.criterion.*;
import com.alzzaipo.hexagonal.notification.domain.criterion.NotificationCriterion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class NotificationCriterionPersistenceAdapter implements
        RegisterNotificationCriterionPort,
        FindMemberNotificationCriteriaPort,
        FindNotificationCriterionPort,
        UpdateNotificationCriterionPort,
        DeleteNotificationCriterionPort,
        FindAllNotificationCriterionPort {

    private final NewNotificationCriterionRepository notificationCriterionRepository;
    private final NewMemberRepository memberRepository;

    @Override
    public void registerNotificationCriterion(NotificationCriterion notificationCriterion) {
        MemberJpaEntity memberJpaEntity = memberRepository.findByUid(notificationCriterion.getMemberUID().get())
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));

        NotificationCriterionJpaEntity entity = toJpaEntity(notificationCriterion, memberJpaEntity);
        notificationCriterionRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationCriterion> findMemberNotificationCriteria(Uid memberUID) {
        return notificationCriterionRepository.findByMemberUID(memberUID.get())
                .stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationCriterion> findNotificationCriterion(Uid notifcationCriterionUID) {
        return notificationCriterionRepository.findByNotificationCriterionUID(notifcationCriterionUID.get())
                .map(this::toDomainEntity);
    }

    @Override
    public void updateNotificationCriterion(UpdateNotificationCriterionCommand command) {
        Long uid = command.getNotificationCriterionUID().get();

        NotificationCriterionJpaEntity entity = notificationCriterionRepository.findByNotificationCriterionUID(uid)
                .orElseThrow(() -> new RuntimeException("알림 기준 조회 실패"));

        entity.changeMinCompetitionRate(command.getMinCompetitionRate());
        entity.changeMinLockupRate(command.getMinLockupRate());
    }

    @Override
    public void deleteNotificationCriterion(Uid notificationCriterionUID) {
        Long uid = notificationCriterionUID.get();

        NotificationCriterionJpaEntity entity = notificationCriterionRepository.findByNotificationCriterionUID(uid)
                .orElseThrow(() -> new RuntimeException("알림 기준 조회 실패"));

        notificationCriterionRepository.delete(entity);
    }

    @Override
    public List<NotificationCriterion> findAllNotificationCriterion() {
        return notificationCriterionRepository.findAll()
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
