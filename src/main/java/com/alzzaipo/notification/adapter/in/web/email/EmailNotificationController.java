package com.alzzaipo.notification.adapter.in.web.email;

import com.alzzaipo.common.Email;
import com.alzzaipo.common.MemberPrincipal;
import com.alzzaipo.notification.adapter.in.web.email.dto.EmailDto;
import com.alzzaipo.notification.application.port.dto.EmailNotificationStatus;
import com.alzzaipo.notification.application.port.dto.SubscribeEmailNotificationCommand;
import com.alzzaipo.notification.application.port.in.email.FindEmailNotificationStatusQuery;
import com.alzzaipo.notification.application.port.in.email.SubscribeEmailNotificationUseCase;
import com.alzzaipo.notification.application.port.in.email.UnsubscribeEmailNotificationUseCase;
import com.alzzaipo.notification.application.port.in.email.UpdateEmailNotificationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/notification/email")
@RequiredArgsConstructor
public class EmailNotificationController {

	private final SubscribeEmailNotificationUseCase subscribeEmailNotificationUseCase;
	private final FindEmailNotificationStatusQuery findEmailNotificationStatusQuery;
	private final UpdateEmailNotificationUseCase updateEmailNotificationUseCase;
	private final UnsubscribeEmailNotificationUseCase unsubscribeEmailNotificationUseCase;

	@PostMapping("/subscribe")
	public ResponseEntity<String> subscribeEmailNotification(
		@AuthenticationPrincipal MemberPrincipal principal,
		@Valid @RequestBody EmailDto emailDto) {

		SubscribeEmailNotificationCommand command
			= createSubscribeEmailNotificationCommand(principal, emailDto);

		subscribeEmailNotificationUseCase.subscribeEmailNotification(command);

		return ResponseEntity.ok("신청 완료");
	}

	@GetMapping("/status")
	public ResponseEntity<EmailNotificationStatus> findEmailNotificationStatus(
		@AuthenticationPrincipal MemberPrincipal principal) {

		EmailNotificationStatus emailNotificationStatus
			= findEmailNotificationStatusQuery.findEmailNotificationStatus(
			principal.getMemberUID());

		return ResponseEntity.ok(emailNotificationStatus);
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateEmailNotification(
		@AuthenticationPrincipal MemberPrincipal principal,
		@Valid @RequestBody EmailDto dto) {

		updateEmailNotificationUseCase.updateEmailNotification(
			principal.getMemberUID(),
			new Email(dto.getEmail()));

		return ResponseEntity.ok("수정 완료");
	}

	@DeleteMapping("/unsubscribe")
	public ResponseEntity<String> unsubscribeEmailNotification(
		@AuthenticationPrincipal MemberPrincipal principal) {

		unsubscribeEmailNotificationUseCase.unsubscribeEmailNotification(principal.getMemberUID());

		return ResponseEntity.ok("해지 완료");
	}

	private SubscribeEmailNotificationCommand createSubscribeEmailNotificationCommand(
		MemberPrincipal principal, EmailDto emailDto) {

		return new SubscribeEmailNotificationCommand(
			principal.getMemberUID(),
			principal.getCurrentLoginType(),
			new Email(emailDto.getEmail()));
	}
}
