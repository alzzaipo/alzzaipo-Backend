package com.alzzaipo.hexagonal.notification.adapter.out.persistence.email;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.notification.application.port.out.email.FindEmailNotificationPort;
import com.alzzaipo.hexagonal.notification.domain.email.EmailNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmailNotificationPersistenceAdapter implements FindEmailNotificationPort {

    private final NewEmailNotificationRepository emailNotificationRepository;

    @Override
    public Optional<EmailNotification> findEmailNotification(Uid memberUID) {
        return emailNotificationRepository.findByMemberUID(memberUID.get())
                .map(this::toDomainEntity);
    }

    private EmailNotification toDomainEntity(EmailNotificationJpaEntity jpaEntity) {
        return new EmailNotification(
                new Uid(jpaEntity.getMemberJpaEntity().getUid()),
                new Email(jpaEntity.getEmail()));
    }
}
