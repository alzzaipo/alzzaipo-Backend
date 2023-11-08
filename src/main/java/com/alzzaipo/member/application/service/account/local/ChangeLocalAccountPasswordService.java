package com.alzzaipo.member.application.service.account.local;

import com.alzzaipo.member.application.port.in.dto.ChangeLocalAccountPasswordCommand;
import com.alzzaipo.member.application.port.in.account.local.ChangeLocalAccountPasswordUseCase;
import com.alzzaipo.member.application.port.out.account.local.ChangeLocalAccountPasswordPort;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByMemberUidPort;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;
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
