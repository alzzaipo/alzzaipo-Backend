package com.alzzaipo.hexagonal.notification.adapter.out.persistence.email;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.member.NewMemberRepository;
import com.alzzaipo.hexagonal.notification.application.port.out.email.FindEmailNotificationPort;
import com.alzzaipo.hexagonal.notification.application.port.out.email.RegisterEmailNotificationPort;
import com.alzzaipo.hexagonal.notification.application.port.out.email.UpdateEmailNotificataionPort;
import com.alzzaipo.hexagonal.notification.domain.email.EmailNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
public class EmailNotificationPersistenceAdapter implements
        FindEmailNotificationPort,
        RegisterEmailNotificationPort,
        UpdateEmailNotificataionPort {

    private final NewEmailNotificationRepository emailNotificationRepository;
    private final NewMemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<EmailNotification> findEmailNotification(Uid memberUID) {
        return emailNotificationRepository.findByMemberUID(memberUID.get())
                .map(this::toDomainEntity);
    }

    @Override
    public void registerEmailNotification(EmailNotification emailNotification) {
        MemberJpaEntity memberJpaEntity = memberRepository.findByUid(emailNotification.getMemberUID().get())
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));

        EmailNotificationJpaEntity emailNotificationJpaEntity = toJpaEntity(emailNotification, memberJpaEntity);
        emailNotificationRepository.save(emailNotificationJpaEntity);
    }

    @Override
    public void updateEmailNotificataion(EmailNotification emailNotification) {
        EmailNotificationJpaEntity entity =
                emailNotificationRepository.findByMemberUID(emailNotification.getMemberUID().get())
                        .orElseThrow(() -> new RuntimeException("이메일 알림 조회 실패"));

        entity.changeEmail(emailNotification.getEmail().get());
    }

    private EmailNotification toDomainEntity(EmailNotificationJpaEntity jpaEntity) {
        return new EmailNotification(
                new Uid(jpaEntity.getMemberJpaEntity().getUid()),
                new Email(jpaEntity.getEmail()));
    }

    private EmailNotificationJpaEntity toJpaEntity(EmailNotification domainEntity, MemberJpaEntity memberJpaEntity) {
        return new EmailNotificationJpaEntity(
                domainEntity.getEmail().get(),
                memberJpaEntity);
    }
}
