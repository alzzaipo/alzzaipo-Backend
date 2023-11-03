package com.alzzaipo.hexagonal.email.application.service;

import com.alzzaipo.hexagonal.email.application.port.in.SendEmailVerificationCodeUseCase;
import com.alzzaipo.hexagonal.email.application.port.out.DeleteOldEmailVerificationHistoryPort;
import com.alzzaipo.hexagonal.email.domain.EmailVerificationCode;
import com.alzzaipo.hexagonal.email.application.port.out.SaveEmailVerificationHistoryPort;
import com.alzzaipo.hexagonal.email.application.port.out.SendEmailVerificationCodePort;
import com.alzzaipo.hexagonal.common.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendEmailVerificationCodeService implements SendEmailVerificationCodeUseCase {

    private final SendEmailVerificationCodePort sendEmailVerificationCodePort;
    private final SaveEmailVerificationHistoryPort saveEmailVerificationHistoryPort;
    private final DeleteOldEmailVerificationHistoryPort deleteOldEmailVerificationHistoryPort;

    @Override
    public void sendEmailVerificationCode(Email email) {
        EmailVerificationCode sentEmailVerificationCode =
                sendEmailVerificationCodePort.sendEmailVerificationCode(email);

        deleteOldEmailVerificationHistoryPort.deleteOldEmailVerificationHistory(email);

        saveEmailVerificationHistoryPort.saveEmailVerificationHistory(email, sentEmailVerificationCode);
    }
}
