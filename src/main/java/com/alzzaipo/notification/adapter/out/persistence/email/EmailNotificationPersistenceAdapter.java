package com.alzzaipo.notification.adapter.out.persistence.email;

import com.alzzaipo.common.Email;
import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.member.adapter.out.persistence.member.MemberRepository;
import com.alzzaipo.notification.application.port.out.UnsubscribeEmailNotificationPort;
import com.alzzaipo.notification.application.port.out.email.FindEmailNotificationPort;
import com.alzzaipo.notification.application.port.out.email.RegisterEmailNotificationPort;
import com.alzzaipo.notification.application.port.out.email.UpdateEmailNotificataionPort;
import com.alzzaipo.notification.domain.email.EmailNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
public class EmailNotificationPersistenceAdapter implements
        FindEmailNotificationPort,
        RegisterEmailNotificationPort,
        UpdateEmailNotificataionPort,
        UnsubscribeEmailNotificationPort {

    private final EmailNotificationRepository emailNotificationRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<EmailNotification> findEmailNotification(Uid memberUID) {
        return emailNotificationRepository.findByMemberUID(memberUID.get())
                .map(this::toDomainEntity);
    }

    @Override
    public void registerEmailNotification(EmailNotification emailNotification) {
        MemberJpaEntity memberJpaEntity = memberRepository.findByUid(emailNotification.getMemberUID().get())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "회원 조회 실패"));

        EmailNotificationJpaEntity emailNotificationJpaEntity = toJpaEntity(emailNotification, memberJpaEntity);
        emailNotificationRepository.save(emailNotificationJpaEntity);
    }

    @Override
    public void updateEmailNotificataion(EmailNotification emailNotification) {
        EmailNotificationJpaEntity entity =
                emailNotificationRepository.findByMemberUID(emailNotification.getMemberUID().get())
                        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "이메일 알림 조회 실패"));

        entity.changeEmail(emailNotification.getEmail().get());
    }

    @Override
    public void unsubscribeEmailNotification(Uid memberUID) {
        EmailNotificationJpaEntity entity =
                emailNotificationRepository.findByMemberUID(memberUID.get())
                        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "이메일 알림 조회 실패"));

        emailNotificationRepository.delete(entity);
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
