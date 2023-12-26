package com.alzzaipo.member.application.service.member;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Uid;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.common.email.port.out.verification.CheckEmailVerifiedPort;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.application.port.in.dto.MemberProfile;
import com.alzzaipo.member.application.port.in.dto.UpdateMemberProfileCommand;
import com.alzzaipo.member.application.port.in.member.FindMemberAccountEmailsQuery;
import com.alzzaipo.member.application.port.in.member.FindMemberNicknameQuery;
import com.alzzaipo.member.application.port.in.member.FindMemberProfileQuery;
import com.alzzaipo.member.application.port.in.member.UpdateMemberProfileUseCase;
import com.alzzaipo.member.application.port.in.member.WithdrawMemberUseCase;
import com.alzzaipo.member.application.port.out.account.local.ChangeLocalAccountEmailPort;
import com.alzzaipo.member.application.port.out.account.local.CheckLocalAccountEmailAvailablePort;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByMemberUidPort;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.member.application.port.out.member.ChangeMemberNicknamePort;
import com.alzzaipo.member.application.port.out.member.FindMemberAccountEmailsPort;
import com.alzzaipo.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.member.application.port.out.member.FindMemberProfilePort;
import com.alzzaipo.member.application.port.out.member.WithdrawMemberPort;
import com.alzzaipo.member.domain.member.Member;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements FindMemberNicknameQuery,
	FindMemberProfileQuery,
	UpdateMemberProfileUseCase,
	FindMemberAccountEmailsQuery,
	WithdrawMemberUseCase {

	private final FindMemberPort findMemberPort;
	private final FindMemberProfilePort findMemberProfilePort;
	private final CheckLocalAccountEmailAvailablePort checkLocalAccountEmailAvailablePort;
	private final FindLocalAccountByMemberUidPort findLocalAccountByMemberUidPort;
	private final ChangeMemberNicknamePort changeMemberNicknamePort;
	private final ChangeLocalAccountEmailPort changeLocalAccountEmailPort;
	private final CheckEmailVerifiedPort checkEmailVerifiedPort;
	private final FindMemberAccountEmailsPort findMemberAccountEmailsPort;
	private final WithdrawMemberPort withdrawMemberPort;

	@Override
	@Transactional(readOnly = true)
	public String findMemberNickname(Uid memberUID) {
		Member member = findMemberPort.findMember(memberUID);
		return member.getNickname();
	}

	@Override
	@Transactional(readOnly = true)
	public MemberProfile findMemberProfile(Uid memberUID, LoginType currentLoginType) {
		return findMemberProfilePort.findProfile(memberUID.get(), currentLoginType);
	}

	@Override
	public void updateMemberProfile(UpdateMemberProfileCommand command) {
		changeMemberNicknamePort.changeMemberNickname(command.getMemberUID(), command.getNickname());

		findLocalAccountByMemberUidPort.findByMemberId(command.getMemberUID())
			.ifPresent(localAccount -> changeLocalAccountEmail(command, localAccount));
	}

	@Override
	public Set<String> findEmails(Uid memberId) {
		return findMemberAccountEmailsPort.findEmails(memberId);
	}

	@Override
	public void withdrawMember(Uid memberUID) {
		withdrawMemberPort.withdrawMember(memberUID);
	}

	private void changeLocalAccountEmail(UpdateMemberProfileCommand command, SecureLocalAccount localAccount) {
		if (localAccount.getEmail().equals(command.getEmail())) {
			return;
		}
		checkEmailAvailable(command.getEmail());
		checkEmailVerified(command.getEmail(), command.getMemberUID());

		changeLocalAccountEmailPort.changeLocalAccountEmail(localAccount.getAccountId(), command.getEmail());
	}

	private void checkEmailAvailable(Email email) {
		boolean isAvailable = checkLocalAccountEmailAvailablePort.checkEmailAvailable(email.get());
		if (!isAvailable) {
			throw new CustomException(HttpStatus.CONFLICT, "사용중인 이메일");
		}
	}

	private void checkEmailVerified(Email email, Uid memberId) {
		boolean isVerified = checkEmailVerifiedPort.check(email.get(),
			memberId.toString(),
			EmailVerificationPurpose.UPDATE);

		if (!isVerified) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "미인증 이메일");
		}
	}
}
