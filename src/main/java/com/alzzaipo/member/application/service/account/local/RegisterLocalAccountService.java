package com.alzzaipo.member.application.service.account.local;

import com.alzzaipo.email.application.port.in.CheckEmailVerifiedQuery;
import com.alzzaipo.email.application.port.out.DeleteOldEmailVerificationHistoryPort;
import com.alzzaipo.common.Email;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountEmailAvailabilityQuery;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountIdAvailabilityQuery;
import com.alzzaipo.member.application.port.in.dto.RegisterLocalAccountCommand;
import com.alzzaipo.member.application.port.in.account.local.RegisterLocalAccountUseCase;
import com.alzzaipo.member.application.port.out.account.local.RegisterLocalAccountPort;
import com.alzzaipo.member.application.port.out.member.RegisterMemberPort;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import com.alzzaipo.member.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterLocalAccountService implements RegisterLocalAccountUseCase {

    private final PasswordEncoder passwordEncoder;

    private final CheckLocalAccountIdAvailabilityQuery checkLocalAccountIdAvailabilityQuery;
    private final CheckLocalAccountEmailAvailabilityQuery checkLocalAccountEmailAvailabilityQuery;
    private final CheckEmailVerifiedQuery checkEmailVerifiedQuery;
    private final RegisterMemberPort registerMemberPort;
    private final RegisterLocalAccountPort registerLocalAccountPort;
    private final DeleteOldEmailVerificationHistoryPort deleteOldEmailVerificationHistoryPort;

    @Override
    public void registerLocalAccount(RegisterLocalAccountCommand command) {
        String plainLocalAccountPassword = command.getLocalAccountPassword().get();
        String encryptedLocalAccountPassword = passwordEncoder.encode(plainLocalAccountPassword);

        checkAccountIdAvailability(command.getLocalAccountId());
        checkEmailAvailability(command.getEmail());
        checkEmailVerified(command.getEmail());

        Member member = Member.create(command.getNickname());

        SecureLocalAccount secureLocalAccount = new SecureLocalAccount(
                member.getUid(),
                command.getLocalAccountId(),
                encryptedLocalAccountPassword,
                command.getEmail());

        registerMemberPort.registerMember(member);
        registerLocalAccountPort.registerLocalAccountPort(secureLocalAccount);

        deleteOldEmailVerificationHistoryPort.deleteOldEmailVerificationHistory(command.getEmail());
    }

    private void checkAccountIdAvailability(LocalAccountId localAccountId) {
        if (!checkLocalAccountIdAvailabilityQuery.checkLocalAccountIdAvailability(localAccountId)) {
            throw new IllegalArgumentException("아이디 중복");
        }
    }

    private void checkEmailAvailability(Email email) {
        if (!checkLocalAccountEmailAvailabilityQuery.checkLocalAccountEmailAvailability(email)) {
            throw new IllegalArgumentException("이메일 중복");
        }
    }

    private void checkEmailVerified(Email email) {
        if (!checkEmailVerifiedQuery.checkEmailVerified(email)) {
            throw new IllegalArgumentException("인증되지 않은 이메일");
        }
    }
}
