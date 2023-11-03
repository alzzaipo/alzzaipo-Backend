package com.alzzaipo.hexagonal.member.application.service;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.email.application.port.in.CheckEmailVerifiedQuery;
import com.alzzaipo.hexagonal.email.application.port.out.DeleteOldEmailVerificationHistoryPort;
import com.alzzaipo.hexagonal.member.application.port.in.CheckLocalAccountEmailAvailabilityQuery;
import com.alzzaipo.hexagonal.member.application.port.in.CheckLocalAccountIdAvailabilityQuery;
import com.alzzaipo.hexagonal.member.application.port.in.RegisterLocalAccountCommand;
import com.alzzaipo.hexagonal.member.application.port.in.RegisterLocalAccountUseCase;
import com.alzzaipo.hexagonal.member.application.port.out.RegisterLocalAccountPort;
import com.alzzaipo.hexagonal.member.application.port.out.RegisterMemberPort;
import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccount;
import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountId;
import com.alzzaipo.hexagonal.member.domain.Member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterLocalAccountService implements RegisterLocalAccountUseCase {

    private final CheckLocalAccountIdAvailabilityQuery checkLocalAccountIdAvailabilityQuery;
    private final CheckLocalAccountEmailAvailabilityQuery checkLocalAccountEmailAvailabilityQuery;
    private final CheckEmailVerifiedQuery checkEmailVerifiedQuery;
    private final RegisterMemberPort registerMemberPort;
    private final RegisterLocalAccountPort registerLocalAccountPort;
    private final DeleteOldEmailVerificationHistoryPort deleteOldEmailVerificationHistoryPort;


    @Override
    public void registerLocalAccount(RegisterLocalAccountCommand command) {
        checkAccountIdAvailability(command.getLocalAccountId());
        checkEmailAvailability(command.getEmail());
        checkEmailVerified(command.getEmail());

        Member member = Member.create(command.getNickname());

        LocalAccount localAccount = new LocalAccount(
                member.getUid(),
                command.getLocalAccountId(),
                command.getLocalAccountPassword(),
                command.getEmail());

        registerMemberPort.registerMember(member);
        registerLocalAccountPort.registerLocalAccountPort(localAccount);

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
