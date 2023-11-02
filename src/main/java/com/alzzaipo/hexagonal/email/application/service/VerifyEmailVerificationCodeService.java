package com.alzzaipo.hexagonal.email.application.service;

import com.alzzaipo.hexagonal.email.application.port.in.VerifyEmailVerificationCodeUseCase;
import com.alzzaipo.hexagonal.email.domain.EmailVerificationCode;
import com.alzzaipo.hexagonal.email.application.port.out.FindEmailVerificationCodePort;
import com.alzzaipo.hexagonal.email.application.port.out.SetEmailVerificationHistoryVerifiedPort;
import com.alzzaipo.hexagonal.common.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VerifyEmailVerificationCodeService implements VerifyEmailVerificationCodeUseCase {

    private final FindEmailVerificationCodePort findEmailVerificationCodePort;
    private final SetEmailVerificationHistoryVerifiedPort setEmailVerificationHistoryVerifiedPort;

    @Override
    public boolean verifyEmailVerificationCode(Email email, EmailVerificationCode verificationCode) {
        String userProvidedVerificationCode = verificationCode.get();

        Optional<String> validVerificationCode =
                findEmailVerificationCodePort.findEmailVerificationCode(email);

        if(validVerificationCode.isPresent() &&
                userProvidedVerificationCode.equals(validVerificationCode.get()))
        {
            setEmailVerificationHistoryVerifiedPort.setEmailVerificationHistoryVerified(email);
            return true;
        }

        return false;
    }
}
