package com.alzzaipo.member.adapter.in.web;

import com.alzzaipo.common.MemberPrincipal;
import com.alzzaipo.common.Uid;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.member.adapter.in.web.dto.ChangeLocalAccountPasswordWebRequest;
import com.alzzaipo.member.adapter.in.web.dto.EmailDto;
import com.alzzaipo.member.adapter.in.web.dto.EmailVerificationCodeDto;
import com.alzzaipo.member.adapter.in.web.dto.LocalAccountPasswordDto;
import com.alzzaipo.member.adapter.in.web.dto.RegisterLocalAccountWebRequest;
import com.alzzaipo.member.adapter.in.web.dto.SendSignUpEmailVerificationCodeWebRequest;
import com.alzzaipo.member.adapter.in.web.dto.UpdateMemberProfileWebRequest;
import com.alzzaipo.member.application.port.in.account.local.ChangeLocalAccountPasswordUseCase;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountEmailAvailabilityQuery;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountIdAvailabilityQuery;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountPasswordQuery;
import com.alzzaipo.member.application.port.in.account.local.RegisterLocalAccountUseCase;
import com.alzzaipo.member.application.port.in.account.local.SendSignUpEmailVerificationCodeUseCase;
import com.alzzaipo.member.application.port.in.account.local.SendUpdateEmailVerificationCodeUseCase;
import com.alzzaipo.member.application.port.in.account.local.VerifyEmailVerificationCodeUseCase;
import com.alzzaipo.member.application.port.in.dto.ChangeLocalAccountPasswordCommand;
import com.alzzaipo.member.application.port.in.dto.MemberProfile;
import com.alzzaipo.member.application.port.in.dto.RegisterLocalAccountCommand;
import com.alzzaipo.member.application.port.in.dto.SendSignUpEmailVerificationCodeCommand;
import com.alzzaipo.member.application.port.in.dto.UpdateMemberProfileCommand;
import com.alzzaipo.member.application.port.in.member.FindMemberNicknameQuery;
import com.alzzaipo.member.application.port.in.member.FindMemberProfileQuery;
import com.alzzaipo.member.application.port.in.member.UpdateMemberProfileUseCase;
import com.alzzaipo.member.application.port.in.member.WithdrawMemberUseCase;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private final RegisterLocalAccountUseCase registerLocalAccountUseCase;
	private final CheckLocalAccountIdAvailabilityQuery checkLocalAccountIdAvailabilityQuery;
	private final CheckLocalAccountEmailAvailabilityQuery checkLocalAccountEmailAvailabilityQuery;
	private final CheckLocalAccountPasswordQuery checkLocalAccountPasswordQuery;
	private final ChangeLocalAccountPasswordUseCase changeLocalAccountPasswordUseCase;
	private final FindMemberNicknameQuery findMemberNicknameQuery;
	private final FindMemberProfileQuery findMemberProfileQuery;
	private final UpdateMemberProfileUseCase updateMemberProfileUseCase;
	private final WithdrawMemberUseCase withdrawMemberUseCase;
	private final SendSignUpEmailVerificationCodeUseCase sendSignUpEmailVerificationCodeUseCase;
	private final VerifyEmailVerificationCodeUseCase verifyEmailVerificationCodeUseCase;
	private final SendUpdateEmailVerificationCodeUseCase sendUpdateEmailVerificationCodeUseCase;

	@GetMapping("/register/verify-account-id")
	public ResponseEntity<String> checkLocalAccountIdAvailability(@RequestParam("accountId") String accountId) {
		LocalAccountId localAccountId = new LocalAccountId(accountId);
		if (!checkLocalAccountIdAvailabilityQuery.check(localAccountId)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 등록된 아이디 입니다.");
		}
		return ResponseEntity.ok().body("사용 가능한 아이디 입니다.");
	}

	@GetMapping("/register/verify-email")
	public ResponseEntity<String> checkLocalAccountEmailAvailability(@RequestParam("email") String email) {
		Email localAccountEmail = new Email(email);
		if (!checkLocalAccountEmailAvailabilityQuery.check(localAccountEmail)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 등록된 이메일 입니다.");
		}
		return ResponseEntity.ok().body("사용 가능한 이메일 입니다.");
	}

	@PostMapping("/register/send-verification-code")
	public ResponseEntity<String> sendSignUpVerificationCode(
		@Valid @RequestBody SendSignUpEmailVerificationCodeWebRequest dto) {

		SendSignUpEmailVerificationCodeCommand command =
			new SendSignUpEmailVerificationCodeCommand(dto.getAccountId(), dto.getEmail());

		sendSignUpEmailVerificationCodeUseCase.send(command);
		return ResponseEntity.ok().body("전송 완료");
	}

	@PostMapping("/register/validate-verification-code")
	public ResponseEntity<String> validateVerificationCode(@Valid @RequestBody EmailVerificationCodeDto dto) {
		EmailVerificationCode verificationCode = new EmailVerificationCode(dto.getVerificationCode());
		boolean verified = verifyEmailVerificationCodeUseCase.verify(verificationCode, EmailVerificationPurpose.SIGN_UP);
		if (verified) {
			return ResponseEntity.ok().body("인증 성공");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@Valid @RequestBody RegisterLocalAccountWebRequest dto) {
		RegisterLocalAccountCommand command = RegisterLocalAccountCommand.build(dto);
		registerLocalAccountUseCase.registerLocalAccount(command);
		return ResponseEntity.ok().body("가입 완료");
	}

	@PostMapping("/verify-password")
	public ResponseEntity<String> verifyPassword(@AuthenticationPrincipal MemberPrincipal principal,
		@Valid @RequestBody LocalAccountPasswordDto dto) {

		Uid memberUID = principal.getMemberUID();
		LocalAccountPassword localAccountPassword = new LocalAccountPassword(dto.getAccountPassword());

		if (checkLocalAccountPasswordQuery.checkLocalAccountPassword(memberUID, localAccountPassword)) {
			return ResponseEntity.ok().body("비밀번호 검증 성공");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 검증 실패");
	}

	@PostMapping("/change-password")
	public ResponseEntity<String> changePassword(@AuthenticationPrincipal MemberPrincipal principal,
		@Valid @RequestBody ChangeLocalAccountPasswordWebRequest dto) {

		Uid memberUID = principal.getMemberUID();
		LocalAccountPassword currentAccountPassword = new LocalAccountPassword(dto.getCurrentAccountPassword());
		LocalAccountPassword newAccountPassword = new LocalAccountPassword(dto.getNewAccountPassword());

		ChangeLocalAccountPasswordCommand command = new ChangeLocalAccountPasswordCommand(memberUID,
			newAccountPassword);

		if (!checkLocalAccountPasswordQuery.checkLocalAccountPassword(memberUID, currentAccountPassword)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 검증 실패");
		}

		if (changeLocalAccountPasswordUseCase.changeLocalAccountPassword(command)) {
			return ResponseEntity.ok().body("비밀번호 변경 완료");
		}
		return ResponseEntity.internalServerError().body("비밀번호 변경 실패");
	}

	@GetMapping("/nickname")
	public ResponseEntity<String> findMemberNickname(@AuthenticationPrincipal MemberPrincipal principal) {
		String nickname = findMemberNicknameQuery.findMemberNickname(principal.getMemberUID());
		return ResponseEntity.ok().body(nickname);
	}

	@GetMapping("/profile")
	public ResponseEntity<MemberProfile> findMemberProfile(@AuthenticationPrincipal MemberPrincipal principal) {
		MemberProfile memberProfile = findMemberProfileQuery.findMemberProfile(principal.getMemberUID(),
			principal.getCurrentLoginType());

		return ResponseEntity.ok().body(memberProfile);
	}

	@PostMapping("/profile/update/send-verification-code")
	public ResponseEntity<String> sendUpdateEmailVerificationCode(@AuthenticationPrincipal MemberPrincipal principal,
		@Valid @RequestBody EmailDto dto) {
		sendUpdateEmailVerificationCodeUseCase.send(new Email(dto.getEmail()), principal.getMemberUID());
		return ResponseEntity.ok().body("전송 완료");
	}

	@PostMapping("/profile/update/validate-verification-code")
	public ResponseEntity<String> validateRegisterVerificationCode(@Valid @RequestBody EmailVerificationCodeDto dto) {
		EmailVerificationCode emailVerificationCode = new EmailVerificationCode(dto.getVerificationCode());
		if (!verifyEmailVerificationCodeUseCase.verify(emailVerificationCode, EmailVerificationPurpose.UPDATE)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
		}
		return ResponseEntity.ok().body("인증 성공");
	}

	@PutMapping("/profile/update")
	public ResponseEntity<String> updateMemberProfile(@AuthenticationPrincipal MemberPrincipal principal,
		@Valid @RequestBody UpdateMemberProfileWebRequest dto) {

		UpdateMemberProfileCommand command = new UpdateMemberProfileCommand(
			principal.getMemberUID(),
			dto.getNickname(),
			new Email(dto.getEmail()));

		updateMemberProfileUseCase.updateMemberProfile(command);

		return ResponseEntity.ok().body("프로필 수정 완료");
	}

	@DeleteMapping("/withdraw")
	public ResponseEntity<String> withdrawMember(@AuthenticationPrincipal MemberPrincipal principal) {
		withdrawMemberUseCase.withdrawMember(principal.getMemberUID());
		return ResponseEntity.ok().body("회원 탈퇴 완료");
	}
}
