package com.alzzaipo.email.application.service;

import com.alzzaipo.common.Email;
import com.alzzaipo.email.application.port.in.SendEmailVerificationCodeUseCase;
import com.alzzaipo.email.application.port.out.DeleteOldEmailVerificationHistoryPort;
import com.alzzaipo.email.application.port.out.SaveEmailVerificationHistoryPort;
import com.alzzaipo.email.application.port.out.SendEmailVerificationCodePort;
import com.alzzaipo.email.domain.EmailVerificationCode;
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
