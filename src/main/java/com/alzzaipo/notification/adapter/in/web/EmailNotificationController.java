package com.alzzaipo.notification.adapter.in.web;

import com.alzzaipo.common.MemberPrincipal;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.member.adapter.in.web.dto.EmailVerificationCodeDto;
import com.alzzaipo.notification.application.port.dto.EmailNotificationStatus;
import com.alzzaipo.notification.application.port.dto.SubscribeEmailNotificationCommand;
import com.alzzaipo.notification.application.port.in.email.SendNotificationEmailVerificationCodeUseCase;
import com.alzzaipo.notification.application.port.in.email.VerifyNotificationEmailVerificationCodeUseCase;
import com.alzzaipo.notification.application.port.in.email.ChangeNotificationEmailUseCase;
import com.alzzaipo.notification.application.port.in.email.FindEmailNotificationStatusQuery;
import com.alzzaipo.notification.application.port.in.email.SubscribeEmailNotificationUseCase;
import com.alzzaipo.notification.application.port.in.email.UnsubscribeEmailNotificationUseCase;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class EmailNotificationController {

	private final SubscribeEmailNotificationUseCase subscribeEmailNotificationUseCase;
	private final FindEmailNotificationStatusQuery findEmailNotificationStatusQuery;
	private final ChangeNotificationEmailUseCase changeNotificationEmailUseCase;
	private final UnsubscribeEmailNotificationUseCase unsubscribeEmailNotificationUseCase;
	private final SendNotificationEmailVerificationCodeUseCase sendNotificationEmailVerificationCodeUseCase;
	private final VerifyNotificationEmailVerificationCodeUseCase verifyNotificationEmailVerificationCodeUseCase;

	@PostMapping("/send-verification-code")
	public ResponseEntity<String> sendVerificationCode(@AuthenticationPrincipal MemberPrincipal principal,
		@Valid @RequestBody EmailDto dto) {
		sendNotificationEmailVerificationCodeUseCase.send(principal.getMemberUID(), new Email(dto.getEmail()));
		return ResponseEntity.ok().body("전송 완료");
	}

	@PostMapping("/validate-verification-code")
	public ResponseEntity<String> validateVerificationCode(@Valid @RequestBody EmailVerificationCodeDto dto) {
		EmailVerificationCode verificationCode = new EmailVerificationCode(dto.getVerificationCode());
		if (verifyNotificationEmailVerificationCodeUseCase.verify(verificationCode)) {
			return ResponseEntity.ok().body("인증 성공");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
	}

	@PostMapping("/subscribe")
	public ResponseEntity<String> subscribeEmailNotification(@AuthenticationPrincipal MemberPrincipal principal,
		@Valid @RequestBody EmailDto emailDto) {
		SubscribeEmailNotificationCommand command = createSubscribeEmailNotificationCommand(principal, emailDto);
		subscribeEmailNotificationUseCase.subscribe(command);
		return ResponseEntity.ok("신청 완료");
	}

	@GetMapping("/status")
	public ResponseEntity<EmailNotificationStatus> findEmailNotificationStatus(
		@AuthenticationPrincipal MemberPrincipal principal) {
		EmailNotificationStatus emailNotificationStatus = findEmailNotificationStatusQuery.find(principal.getMemberUID());
		return ResponseEntity.ok(emailNotificationStatus);
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateEmailNotification(@AuthenticationPrincipal MemberPrincipal principal,
		@Valid @RequestBody EmailDto dto) {
		changeNotificationEmailUseCase.changeEmail(principal.getMemberUID(), new Email(dto.getEmail()));
		return ResponseEntity.ok("수정 완료");
	}

	@DeleteMapping("/unsubscribe")
	public ResponseEntity<String> unsubscribeEmailNotification(@AuthenticationPrincipal MemberPrincipal principal) {
		unsubscribeEmailNotificationUseCase.unsubscribeEmailNotification(principal.getMemberUID());
		return ResponseEntity.ok("해지 완료");
	}

	private SubscribeEmailNotificationCommand createSubscribeEmailNotificationCommand(MemberPrincipal principal,
		EmailDto emailDto) {
		return new SubscribeEmailNotificationCommand(principal.getMemberUID(), new Email(emailDto.getEmail()));
	}
}
