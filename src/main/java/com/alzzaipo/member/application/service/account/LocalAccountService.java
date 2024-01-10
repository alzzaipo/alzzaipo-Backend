package com.alzzaipo.member.application.service.account;

import com.alzzaipo.common.Id;
import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.common.email.port.out.smtp.SendEmailVerificationCodePort;
import com.alzzaipo.common.email.port.out.verification.CheckEmailVerificationCodePort;
import com.alzzaipo.common.email.port.out.verification.SaveEmailVerificationCodePort;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.common.token.TokenUtil;
import com.alzzaipo.common.token.application.port.out.SaveRefreshTokenPort;
import com.alzzaipo.common.token.domain.TokenInfo;
import com.alzzaipo.member.application.port.in.account.local.ChangeLocalAccountPasswordUseCase;
import com.alzzaipo.member.application.port.in.account.local.CheckEmailVerificationCodeQuery;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountEmailAvailableQuery;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountIdAvailableQuery;
import com.alzzaipo.member.application.port.in.account.local.LocalLoginUseCase;
import com.alzzaipo.member.application.port.in.account.local.RegisterLocalAccountUseCase;
import com.alzzaipo.member.application.port.in.account.local.SendSignUpEmailVerificationCodeUseCase;
import com.alzzaipo.member.application.port.in.account.local.SendUpdateLocalAccountEmailVerificationCodeUseCase;
import com.alzzaipo.member.application.port.in.account.local.VerifyLocalAccountPasswordQuery;
import com.alzzaipo.member.application.port.in.dto.ChangeLocalAccountPasswordCommand;
import com.alzzaipo.member.application.port.in.dto.CheckLocalAccountEmailVerificationCodeCommand;
import com.alzzaipo.member.application.port.in.dto.LocalLoginCommand;
import com.alzzaipo.member.application.port.in.dto.LoginResult;
import com.alzzaipo.member.application.port.in.dto.RegisterLocalAccountCommand;
import com.alzzaipo.member.application.port.in.dto.SendSignUpEmailVerificationCodeCommand;
import com.alzzaipo.member.application.port.out.account.local.ChangeLocalAccountPasswordPort;
import com.alzzaipo.member.application.port.out.account.local.CheckLocalAccountEmailAvailablePort;
import com.alzzaipo.member.application.port.out.account.local.CheckLocalAccountIdAvailablePort;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByIdPort;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByMemberIdPort;
import com.alzzaipo.member.application.port.out.account.local.RegisterLocalAccountPort;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.member.application.port.out.member.RegisterMemberPort;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;
import com.alzzaipo.member.domain.member.Member;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LocalAccountService implements SendSignUpEmailVerificationCodeUseCase,
    CheckEmailVerificationCodeQuery,
    SendUpdateLocalAccountEmailVerificationCodeUseCase,
    CheckLocalAccountEmailAvailableQuery,
    CheckLocalAccountIdAvailableQuery,
    RegisterLocalAccountUseCase,
    VerifyLocalAccountPasswordQuery,
    ChangeLocalAccountPasswordUseCase,
    LocalLoginUseCase {

    private final PasswordEncoder passwordEncoder;
    private final SendEmailVerificationCodePort sendEmailVerificationCodePort;
    private final SaveEmailVerificationCodePort saveEmailVerificationCodePort;
    private final CheckLocalAccountEmailAvailablePort checkLocalAccountEmailAvailablePort;
    private final CheckLocalAccountIdAvailablePort checkLocalAccountIdAvailablePort;
    private final CheckEmailVerificationCodePort checkEmailVerificationCodePort;
    private final RegisterMemberPort registerMemberPort;
    private final RegisterLocalAccountPort registerLocalAccountPort;
    private final ChangeLocalAccountPasswordPort changeLocalAccountPasswordPort;
    private final SaveRefreshTokenPort saveRefreshTokenPort;
    private final FindLocalAccountByIdPort findLocalAccountByIdPort;
    private final FindLocalAccountByMemberIdPort findLocalAccountByMemberIdPort;

    @Override
    public void sendSignUpEmailVerificationCode(SendSignUpEmailVerificationCodeCommand command) {
        String email = command.getEmail().get();

        if (!checkLocalAccountEmailAvailablePort.checkEmailAvailable(email)) {
            throw new CustomException(HttpStatus.CONFLICT, "이메일 중복");
        }

        String sentVerificationCode = sendEmailVerificationCodePort.sendVerificationCode(email);

        saveEmailVerificationCodePort.save(email, sentVerificationCode, EmailVerificationPurpose.SIGN_UP);
    }

    @Override
    public boolean checkEmailVerificationCode(CheckLocalAccountEmailVerificationCodeCommand command) {
        return checkEmailVerificationCodePort.check(
            command.getEmail(),
            command.getVerificationCode(),
            command.getEmailVerificationPurpose());
    }

    @Override
    public void sendUpdateLocalAccountEmailVerificationCode(Email email) {
        if (!checkLocalAccountEmailAvailablePort.checkEmailAvailable(email.get())) {
            throw new CustomException(HttpStatus.CONFLICT, "이메일 중복");
        }

        String verificationCode = sendEmailVerificationCodePort.sendVerificationCode(email.get());

        saveEmailVerificationCodePort.save(email.get(), verificationCode, EmailVerificationPurpose.UPDATE);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkEmailAvailable(Email email) {
        return checkLocalAccountEmailAvailablePort.checkEmailAvailable(email.get());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkAccountIdAvailable(LocalAccountId localAccountId) {
        return checkLocalAccountIdAvailablePort.checkAccountIdAvailable(localAccountId.get());
    }

    @Override
    public void registerLocalAccount(RegisterLocalAccountCommand command) {
        checkRegistrationPossible(command);

        Member member = Member.build(command.getNickname());

        SecureLocalAccount secureLocalAccount = new SecureLocalAccount(member.getId(),
            command.getLocalAccountId(),
            passwordEncoder.encode(command.getLocalAccountPassword().get()),
            command.getEmail());

        registerMemberPort.registerMember(member);
        registerLocalAccountPort.registerLocalAccountPort(secureLocalAccount);
    }

    private void checkRegistrationPossible(RegisterLocalAccountCommand command) {
        if (!checkLocalAccountIdAvailablePort.checkAccountIdAvailable(command.getLocalAccountId().get())) {
            throw new CustomException(HttpStatus.CONFLICT, "아이디 중복");
        }

        if (!checkLocalAccountEmailAvailablePort.checkEmailAvailable(command.getEmail().get())) {
            throw new CustomException(HttpStatus.CONFLICT, "이메일 중복");
        }

        if (!checkEmailVerificationCodePort.check(command.getEmail().get(), command.getEmailVerificationCode().get(),
            EmailVerificationPurpose.SIGN_UP)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "이메일 미인증");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifyLocalAccountPassword(Id memberId, LocalAccountPassword password) {
        Optional<SecureLocalAccount> localAccount = findLocalAccountByMemberIdPort.findByMemberId(memberId);

        return localAccount.isPresent() &&
            passwordEncoder.matches(password.get(), localAccount.get().getEncryptedAccountPassword());
    }

    @Override
    public boolean changePassword(ChangeLocalAccountPasswordCommand command) {
        Long memberId = command.getMemberId().get();
        String newPassword = command.getNewPassword().get();

        if (verifyLocalAccountPassword(command.getMemberId(), command.getCurrentPassword())) {
            changeLocalAccountPasswordPort.changePassword(memberId, passwordEncoder.encode(newPassword));
            return true;
        }
        return false;
    }

    @Override
    public LoginResult handleLocalLogin(LocalLoginCommand command) {
        Optional<SecureLocalAccount> localAccount = findLocalAccountByIdPort.findLocalAccountById(
            command.getLocalAccountId());

        if (localAccount.isPresent() && isPasswordValid(command, localAccount.get())) {
            Id memberId = localAccount.get().getMemberId();
            TokenInfo tokenInfo = TokenUtil.createToken(memberId, LoginType.LOCAL);
            saveRefreshTokenPort.save(tokenInfo.getRefreshToken(), memberId);
            return new LoginResult(true, tokenInfo);
        }
        return LoginResult.getFailedResult();
    }

    private boolean isPasswordValid(LocalLoginCommand command, SecureLocalAccount secureLocalAccount) {
        String userProvidedPassword = command.getLocalAccountPassword().get();
        String validEncryptedPassword = secureLocalAccount.getEncryptedAccountPassword();
        return passwordEncoder.matches(userProvidedPassword, validEncryptedPassword);
    }
}
