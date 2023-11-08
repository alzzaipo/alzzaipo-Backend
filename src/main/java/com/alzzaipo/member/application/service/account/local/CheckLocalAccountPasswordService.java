package com.alzzaipo.member.application.service.account.local;

import com.alzzaipo.common.Uid;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountPasswordQuery;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByMemberUidPort;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CheckLocalAccountPasswordService implements CheckLocalAccountPasswordQuery {

    private final PasswordEncoder passwordEncoder;
    private final FindLocalAccountByMemberUidPort findLocalAccountByMemberUidPort;

    @Override
    public boolean checkLocalAccountPassword(Uid memberUID, LocalAccountPassword localAccountPassword) {
        Optional<SecureLocalAccount> optionalSecureLocalAccount
                = findLocalAccountByMemberUidPort.findLocalAccountByMemberUid(memberUID);

        return optionalSecureLocalAccount.isPresent()
                && isPasswordValid(localAccountPassword, optionalSecureLocalAccount.get());
    }

    private boolean isPasswordValid(LocalAccountPassword userProvidedPassword, SecureLocalAccount secureLocalAccount) {
        String validEncryptedPassword = secureLocalAccount.getEncryptedAccountPassword();
        return passwordEncoder.matches(userProvidedPassword.get(), validEncryptedPassword);
    }
}
