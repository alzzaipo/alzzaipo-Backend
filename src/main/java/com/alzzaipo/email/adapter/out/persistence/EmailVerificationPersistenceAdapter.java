package com.alzzaipo.email.adapter.out.persistence;

import com.alzzaipo.common.Email;
import com.alzzaipo.email.application.port.out.*;
import com.alzzaipo.email.domain.EmailVerificationCode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
public class EmailVerificationPersistenceAdapter implements
        SaveEmailVerificationHistoryPort,
        FindEmailVerificationCodePort,
        DeleteOldEmailVerificationHistoryPort,
        SetEmailVerificationHistoryVerifiedPort,
        CheckEmailVerifiedPort {

    private final EntityManager entityManager;
    private final EmailVerificationHistoryRepository emailVerificationHistoryRepository;

    @Override
    public void saveEmailVerificationHistory(Email email, EmailVerificationCode verificationCode) {
        EmailVerificationHistoryJpaEntity entity
                = new EmailVerificationHistoryJpaEntity(email.get(), verificationCode.get());

        emailVerificationHistoryRepository.save(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<String> findEmailVerificationCode(Email email) {
        return emailVerificationHistoryRepository.findByEmail(email.get())
                .map(EmailVerificationHistoryJpaEntity::getVerificationCode);
    }

    @Override
    public void deleteOldEmailVerificationHistory(Email email) {
        emailVerificationHistoryRepository.findByEmail(email.get())
                .ifPresent(entity -> {
                    emailVerificationHistoryRepository.delete(entity);
                    entityManager.flush();
                });
    }

    @Override
    public void setEmailVerificationHistoryVerified(Email email) {
        emailVerificationHistoryRepository.findByEmail(email.get())
                .ifPresent(entity -> {
                    entity.setVerified();
                    entityManager.flush();
                });
    }

    @Override
    public boolean checkEmailVerified(Email email) {
        Optional<EmailVerificationHistoryJpaEntity> emailVerificationHistoryJpaEntity
                = emailVerificationHistoryRepository.findByEmail(email.get());

        return emailVerificationHistoryJpaEntity.isPresent()
                && emailVerificationHistoryJpaEntity.get().isVerified();
    }
}
