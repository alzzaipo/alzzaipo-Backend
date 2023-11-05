package com.alzzaipo.hexagonal.member.application.service;

import com.alzzaipo.hexagonal.common.LoginType;
import com.alzzaipo.hexagonal.common.jwt.NewJwtUtil;
import com.alzzaipo.hexagonal.member.application.port.in.dto.LocalLoginCommand;
import com.alzzaipo.hexagonal.member.application.port.in.dto.LoginResult;
import com.alzzaipo.hexagonal.member.application.port.in.LocalLoginUseCase;
import com.alzzaipo.hexagonal.member.application.port.out.FindLocalAccountByAccountIdPort;
import com.alzzaipo.hexagonal.member.application.port.out.dto.SecureLocalAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LocalLoginService implements LocalLoginUseCase {

    private final NewJwtUtil newJwtUtil;
    private final PasswordEncoder passwordEncoder;

    private final FindLocalAccountByAccountIdPort findLocalAccountByAccountIdPort;

    @Override
    public LoginResult handleLocalLogin(LocalLoginCommand command) {
        Optional<SecureLocalAccount> optionalSecureLocalAccount
                = findLocalAccountByAccountIdPort.findLocalAccountByAccountId(command.getLocalAccountId());

        if (optionalSecureLocalAccount.isPresent() && isPasswordValid(command, optionalSecureLocalAccount.get())) {
            String token = createJwtToken(optionalSecureLocalAccount.get());
            return new LoginResult(true, token);
        }

        return LoginResult.getFailedResult();
    }

    private boolean isPasswordValid(LocalLoginCommand command, SecureLocalAccount secureLocalAccount) {
        String userProvidedPassword = command.getLocalAccountPassword().get();
        String validEncryptedPassword = secureLocalAccount.getEncryptedAccountPassword();
        return passwordEncoder.matches(userProvidedPassword, validEncryptedPassword);
    }

    private String createJwtToken(SecureLocalAccount secureLocalAccount) {
        return newJwtUtil.createToken(secureLocalAccount.getMemberUID(), LoginType.LOCAL);
    }
}
