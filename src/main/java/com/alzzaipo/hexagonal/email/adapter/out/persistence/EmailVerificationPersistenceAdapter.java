package com.alzzaipo.hexagonal.email.adapter.out.persistence;

import com.alzzaipo.hexagonal.email.application.port.out.DeleteOldEmailVerificationHistoryPort;
import com.alzzaipo.hexagonal.email.application.port.out.FindEmailVerificationCodePort;
import com.alzzaipo.hexagonal.email.application.port.out.SaveEmailVerificationHistoryPort;
import com.alzzaipo.hexagonal.email.application.port.out.SetEmailVerificationHistoryVerifiedPort;
import com.alzzaipo.hexagonal.email.domain.Email;
import com.alzzaipo.hexagonal.email.domain.EmailVerificationCode;
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
        SetEmailVerificationHistoryVerifiedPort {

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
}
