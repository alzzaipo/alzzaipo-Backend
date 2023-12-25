package com.alzzaipo.member.application.service.account.local;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.common.email.port.out.smtp.SendEmailVerificationCodePort;
import com.alzzaipo.common.email.port.out.verification.CheckEmailVerifiedPort;
import com.alzzaipo.common.email.port.out.verification.SaveEmailVerificationCodePort;
import com.alzzaipo.common.email.port.out.verification.VerifyEmailVerificationCodePort;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.application.port.in.account.local.ChangeLocalAccountPasswordUseCase;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountEmailAvailableQuery;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountIdAvailabilityQuery;
import com.alzzaipo.member.application.port.in.account.local.RegisterLocalAccountUseCase;
import com.alzzaipo.member.application.port.in.account.local.SendSignUpEmailVerificationCodeUseCase;
import com.alzzaipo.member.application.port.in.account.local.SendUpdateEmailVerificationCodeUseCase;
import com.alzzaipo.member.application.port.in.account.local.VerifyEmailVerificationCodeUseCase;
import com.alzzaipo.member.application.port.in.account.local.VerifyLocalAccountPasswordQuery;
import com.alzzaipo.member.application.port.in.dto.ChangeLocalAccountPasswordCommand;
import com.alzzaipo.member.application.port.in.dto.RegisterLocalAccountCommand;
import com.alzzaipo.member.application.port.in.dto.SendSignUpEmailVerificationCodeCommand;
import com.alzzaipo.member.application.port.out.account.local.ChangeLocalAccountPasswordPort;
import com.alzzaipo.member.application.port.out.account.local.CheckLocalAccountEmailAvailablePort;
import com.alzzaipo.member.application.port.out.account.local.CheckLocalAccountIdAvailablePort;
import com.alzzaipo.member.application.port.out.account.local.RegisterLocalAccountPort;
import com.alzzaipo.member.application.port.out.account.local.VerifyLocalAccountPasswordPort;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.member.application.port.out.member.RegisterMemberPort;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;
import com.alzzaipo.member.domain.member.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalAccountService implements SendSignUpEmailVerificationCodeUseCase,
    VerifyEmailVerificationCodeUseCase,
    SendUpdateEmailVerificationCodeUseCase,
    CheckLocalAccountEmailAvailableQuery,
    CheckLocalAccountIdAvailabilityQuery,
    RegisterLocalAccountUseCase,
    VerifyLocalAccountPasswordQuery,
    ChangeLocalAccountPasswordUseCase {

    private final PasswordEncoder passwordEncoder;
    private final SendEmailVerificationCodePort sendEmailVerificationCodePort;
    private final SaveEmailVerificationCodePort saveEmailVerificationCodePort;
    private final VerifyEmailVerificationCodePort verifyEmailVerificationCodePort;
    private final CheckLocalAccountEmailAvailablePort checkLocalAccountEmailAvailablePort;
    private final CheckLocalAccountIdAvailablePort checkLocalAccountIdAvailablePort;
    private final CheckEmailVerifiedPort checkEmailVerifiedPort;
    private final RegisterMemberPort registerMemberPort;
    private final RegisterLocalAccountPort registerLocalAccountPort;
    private final VerifyLocalAccountPasswordPort verifyLocalAccountPasswordPort;
    private final ChangeLocalAccountPasswordPort changeLocalAccountPasswordPort;

    @Override
    public void sendSignUpEmailVerificationCode(@Valid SendSignUpEmailVerificationCodeCommand command) {
        String accountId = command.getAccountId().get();
        String email = command.getEmail().get();

        if (!checkLocalAccountIdAvailablePort.checkAccountIdAvailable(accountId)) {
            throw new CustomException(HttpStatus.CONFLICT, "아이디 중복");
        }

        String sentVerificationCode = sendEmailVerificationCodePort.sendVerificationCode(email);

        saveEmailVerificationCodePort.save(email, sentVerificationCode, accountId,
            EmailVerificationPurpose.SIGN_UP);
    }

    @Override
    public boolean verifyEmailVerificationCode(@Valid EmailVerificationCode verificationCode,
        EmailVerificationPurpose purpose) {

        return verifyEmailVerificationCodePort.verify(verificationCode.get(), purpose);
    }

    @Override
    public void sendUpdateEmailVerificationCode(@Valid Email email, Uid memberId) {
        String verificationCode = sendEmailVerificationCodePort.sendVerificationCode(email.get());

        saveEmailVerificationCodePort.save(email.get(), verificationCode, memberId.toString(),
            EmailVerificationPurpose.UPDATE);
    }

    @Override
    public boolean checkEmailAvailable(@Valid Email email) {
        return checkLocalAccountEmailAvailablePort.checkEmailAvailable(email.get());
    }

    @Override
    public boolean checkAccountIdAvailable(@Valid LocalAccountId localAccountId) {
        return checkLocalAccountIdAvailablePort.checkAccountIdAvailable(localAccountId.get());
    }

    @Override
    public void registerLocalAccount(@Valid RegisterLocalAccountCommand command) {
        checkAccountIdAvailability(command.getLocalAccountId());
        checkEmailAvailability(command.getEmail());
        checkEmailVerified(command.getEmail(), command.getLocalAccountId());

        Member member = Member.build(command.getNickname());

        SecureLocalAccount secureLocalAccount = new SecureLocalAccount(member.getUid(),
            command.getLocalAccountId(),
            passwordEncoder.encode(command.getLocalAccountPassword().get()),
            command.getEmail());

        registerMemberPort.registerMember(member);
        registerLocalAccountPort.registerLocalAccountPort(secureLocalAccount);
    }

    @Override
    public boolean verifyLocalAccountPassword(Uid memberId, LocalAccountPassword password) {
        return verifyLocalAccountPasswordPort.verifyPassword(memberId.get(),
            passwordEncoder.encode(password.get()));
    }

    @Override
    public boolean changePassword(ChangeLocalAccountPasswordCommand command) {
        Long memberId = command.getMemberUID().get();
        String currentPassword = command.getCurrentPassword().get();
        String newPassword = command.getNewPassword().get();

        if (verifyLocalAccountPasswordPort.verifyPassword(memberId, currentPassword)) {
            changeLocalAccountPasswordPort.changePassword(memberId, passwordEncoder.encode(newPassword));
            return true;
        }
        return false;
    }

    private void checkAccountIdAvailability(LocalAccountId localAccountId) {
        if (!checkLocalAccountIdAvailablePort.checkAccountIdAvailable(localAccountId.get())) {
            throw new CustomException(HttpStatus.CONFLICT, "아이디 중복");
        }
    }

    private void checkEmailAvailability(Email email) {
        if (!checkLocalAccountEmailAvailablePort.checkEmailAvailable(email.get())) {
            throw new CustomException(HttpStatus.CONFLICT, "이메일 중복");
        }
    }

    private void checkEmailVerified(Email email, LocalAccountId localAccountId) {
        boolean isVerified = checkEmailVerifiedPort.check(email.get(), localAccountId.get(),
            EmailVerificationPurpose.SIGN_UP);

        if (!isVerified) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "이메일 미인증");
        }
    }
}
