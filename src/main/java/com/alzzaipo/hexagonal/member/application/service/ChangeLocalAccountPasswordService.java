package com.alzzaipo.hexagonal.member.application.service;

import com.alzzaipo.hexagonal.member.application.port.in.ChangeLocalAccountPasswordCommand;
import com.alzzaipo.hexagonal.member.application.port.in.ChangeLocalAccountPasswordUseCase;
import com.alzzaipo.hexagonal.member.application.port.out.ChangeLocalAccountPasswordPort;
import com.alzzaipo.hexagonal.member.application.port.out.FindLocalAccountByMemberUidPort;
import com.alzzaipo.hexagonal.member.application.port.out.SecureLocalAccount;
import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountId;
import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChangeLocalAccountPasswordService implements ChangeLocalAccountPasswordUseCase {

    private final PasswordEncoder passwordEncoder;
    private final FindLocalAccountByMemberUidPort findLocalAccountByMemberUidPort;
    private final ChangeLocalAccountPasswordPort changeLocalAccountPasswordPort;

    @Override
    public boolean changeLocalAccountPassword(ChangeLocalAccountPasswordCommand command) {
        Optional<SecureLocalAccount> optionalSecureLocalAccount
                = findLocalAccountByMemberUidPort.findLocalAccountByMemberUid(command.getMemberUID());

        return optionalSecureLocalAccount.isPresent() &&
                changePassword(optionalSecureLocalAccount.get(), command.getNewPassword());
    }

    private boolean changePassword(SecureLocalAccount secureLocalAccount, LocalAccountPassword localAccountPassword) {
        LocalAccountId accountId = secureLocalAccount.getAccountId();
        String encryptedNewAccountPassword = passwordEncoder.encode(localAccountPassword.get());

        return changeLocalAccountPasswordPort.changeLocalAccountPassword(accountId, encryptedNewAccountPassword);
    }
}
