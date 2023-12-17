package com.alzzaipo.member.application.service.member;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.common.email.port.out.verification.CheckEmailVerifiedPort;
import com.alzzaipo.common.email.port.out.verification.DeleteEmailVerificationStatusPort;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountEmailAvailabilityQuery;
import com.alzzaipo.member.application.port.in.dto.UpdateMemberProfileCommand;
import com.alzzaipo.member.application.port.in.member.UpdateMemberProfileUseCase;
import com.alzzaipo.member.application.port.out.account.local.ChangeLocalAccountEmail;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByMemberUidPort;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.member.application.port.out.member.ChangeMemberNicknamePort;
import com.alzzaipo.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.member.domain.member.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateMemberProfileService implements UpdateMemberProfileUseCase {

	private static final EmailVerificationPurpose EMAIL_VERIFICATION_PURPOSE = EmailVerificationPurpose.UPDATE;

	private final FindMemberPort findMemberPort;
	private final FindLocalAccountByMemberUidPort findLocalAccountByMemberUidPort;
	private final ChangeMemberNicknamePort changeMemberNicknamePort;
	private final ChangeLocalAccountEmail changeLocalAccountEmail;
	private final CheckLocalAccountEmailAvailabilityQuery checkLocalAccountEmailAvailabilityQuery;
	private final CheckEmailVerifiedPort checkEmailVerifiedPort;
	private final DeleteEmailVerificationStatusPort deleteEmailVerificationStatusPort;

	@Override
	@Transactional
	public void updateMemberProfile(@Valid UpdateMemberProfileCommand command) {
		Member member = findMemberPort.findMember(command.getMemberUID());
		changeMemberNicknamePort.changeMemberNickname(command.getMemberUID(), command.getNickname());
		findLocalAccountByMemberUidPort.findLocalAccountByMemberUid(member.getUid())
			.ifPresent(localAccount -> changeLocalMemberEmail(command, localAccount));
	}

	private void changeLocalMemberEmail(UpdateMemberProfileCommand command, SecureLocalAccount localAccount) {
		Email currentEmail = localAccount.getEmail();
		Email newEmail = command.getEmail();

		// 이메일이 동일한 경우 변경하지 않음
		if (currentEmail.equals(newEmail)) {
			return;
		}

		if (!checkLocalAccountEmailAvailabilityQuery.check(newEmail)) {
			throw new CustomException(HttpStatus.CONFLICT, "오류: 이미 등록된 이메일");
		}
		if (!checkEmailVerifiedPort.check(newEmail.get(), command.getMemberUID().toString(), EMAIL_VERIFICATION_PURPOSE)) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "오류: 인증되지 않은 이메일");
		}

		changeLocalAccountEmail.changeLocalAccountEmail(localAccount.getAccountId(), newEmail);
		deleteEmailVerificationStatusPort.delete(newEmail.get(), EMAIL_VERIFICATION_PURPOSE);
	}
}
