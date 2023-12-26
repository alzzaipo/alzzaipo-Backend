package com.alzzaipo.member.application.service.member;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Id;
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
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByMemberIdPort;
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
	private final FindLocalAccountByMemberIdPort findLocalAccountByMemberIdPort;
	private final ChangeMemberNicknamePort changeMemberNicknamePort;
	private final ChangeLocalAccountEmailPort changeLocalAccountEmailPort;
	private final CheckEmailVerifiedPort checkEmailVerifiedPort;
	private final FindMemberAccountEmailsPort findMemberAccountEmailsPort;
	private final WithdrawMemberPort withdrawMemberPort;

	@Override
	@Transactional(readOnly = true)
	public String findMemberNickname(Id memberId) {
		Member member = findMemberPort.findMember(memberId);
		return member.getNickname();
	}

	@Override
	@Transactional(readOnly = true)
	public MemberProfile findMemberProfile(Id memberId, LoginType currentLoginType) {
		return findMemberProfilePort.findProfile(memberId.get(), currentLoginType);
	}

	@Override
	public void updateMemberProfile(UpdateMemberProfileCommand command) {
		changeMemberNicknamePort.changeMemberNickname(command.getMemberId(), command.getNickname());

		findLocalAccountByMemberIdPort.findByMemberId(command.getMemberId())
			.ifPresent(localAccount -> changeLocalAccountEmail(command, localAccount));
	}

	@Override
	public Set<String> findEmails(Id memberId) {
		return findMemberAccountEmailsPort.findEmails(memberId);
	}

	@Override
	public void withdrawMember(Id memberId) {
		withdrawMemberPort.withdrawMember(memberId);
	}

	private void changeLocalAccountEmail(UpdateMemberProfileCommand command, SecureLocalAccount localAccount) {
		if (localAccount.getEmail().equals(command.getEmail())) {
			return;
		}
		checkEmailAvailable(command.getEmail());
		checkEmailVerified(command.getEmail(), command.getMemberId());

		changeLocalAccountEmailPort.changeLocalAccountEmail(localAccount.getAccountId(), command.getEmail());
	}

	private void checkEmailAvailable(Email email) {
		boolean isAvailable = checkLocalAccountEmailAvailablePort.checkEmailAvailable(email.get());
		if (!isAvailable) {
			throw new CustomException(HttpStatus.CONFLICT, "사용중인 이메일");
		}
	}

	private void checkEmailVerified(Email email, Id memberId) {
		boolean isVerified = checkEmailVerifiedPort.check(email.get(),
			memberId.toString(),
			EmailVerificationPurpose.UPDATE);

		if (!isVerified) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "미인증 이메일");
		}
	}
}
