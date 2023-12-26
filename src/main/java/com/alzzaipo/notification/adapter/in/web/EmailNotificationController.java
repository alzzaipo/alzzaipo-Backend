package com.alzzaipo.notification.adapter.in.web;

import com.alzzaipo.common.MemberPrincipal;
import com.alzzaipo.common.Id;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.member.adapter.in.web.dto.EmailVerificationCodeDto;
import com.alzzaipo.notification.adapter.in.web.dto.EmailDto;
import com.alzzaipo.notification.adapter.in.web.dto.RegisterNotificationCriterionWebRequest;
import com.alzzaipo.notification.adapter.in.web.dto.UpdateNotificationCriterionWebRequest;
import com.alzzaipo.notification.application.port.dto.DeleteNotificationCriterionCommand;
import com.alzzaipo.notification.application.port.dto.EmailNotificationStatus;
import com.alzzaipo.notification.application.port.dto.NotificationCriterionView;
import com.alzzaipo.notification.application.port.dto.RegisterNotificationCriterionCommand;
import com.alzzaipo.notification.application.port.dto.SubscribeEmailNotificationCommand;
import com.alzzaipo.notification.application.port.dto.UpdateNotificationCriterionCommand;
import com.alzzaipo.notification.application.port.in.criterion.DeleteNotificationCriterionUseCase;
import com.alzzaipo.notification.application.port.in.criterion.FindMemberNotificationCriteriaQuery;
import com.alzzaipo.notification.application.port.in.criterion.RegisterNotificationCriterionUseCase;
import com.alzzaipo.notification.application.port.in.criterion.UpdateNotificationCriterionUseCase;
import com.alzzaipo.notification.application.port.in.email.ChangeNotificationEmailUseCase;
import com.alzzaipo.notification.application.port.in.email.CheckNotificationEmailVerificationCodeUseCase;
import com.alzzaipo.notification.application.port.in.email.FindEmailNotificationStatusQuery;
import com.alzzaipo.notification.application.port.in.email.SendNotificationEmailVerificationCodeUseCase;
import com.alzzaipo.notification.application.port.in.email.SubscribeEmailNotificationUseCase;
import com.alzzaipo.notification.application.port.in.email.UnsubscribeEmailNotificationUseCase;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping("/notification")
@RequiredArgsConstructor
public class EmailNotificationController {

	private final SubscribeEmailNotificationUseCase subscribeEmailNotificationUseCase;
	private final FindEmailNotificationStatusQuery findEmailNotificationStatusQuery;
	private final ChangeNotificationEmailUseCase changeNotificationEmailUseCase;
	private final UnsubscribeEmailNotificationUseCase unsubscribeEmailNotificationUseCase;
	private final SendNotificationEmailVerificationCodeUseCase sendNotificationEmailVerificationCodeUseCase;
	private final CheckNotificationEmailVerificationCodeUseCase checkNotificationEmailVerificationCodeUseCase;
	private final RegisterNotificationCriterionUseCase registerNotificationCriterionUseCase;
	private final FindMemberNotificationCriteriaQuery findMemberNotificationCriteriaQuery;
	private final UpdateNotificationCriterionUseCase updateNotificationCriterionUseCase;
	private final DeleteNotificationCriterionUseCase deleteNotificationCriterionUseCase;

	@PostMapping("/send-verification-code")
	public ResponseEntity<String> sendVerificationCode(@AuthenticationPrincipal MemberPrincipal principal,
		@Valid @RequestBody EmailDto dto) {
		sendNotificationEmailVerificationCodeUseCase.sendVerificationCode(principal.getMemberId(), new Email(dto.getEmail()));
		return ResponseEntity.ok().body("전송 완료");
	}

	@PostMapping("/validate-verification-code")
	public ResponseEntity<String> validateVerificationCode(@Valid @RequestBody EmailVerificationCodeDto dto) {
		EmailVerificationCode verificationCode = new EmailVerificationCode(dto.getVerificationCode());
		if (checkNotificationEmailVerificationCodeUseCase.checkVerificationCode(verificationCode)) {
			return ResponseEntity.ok().body("인증 성공");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
	}

	@PostMapping("/subscribe")
	public ResponseEntity<String> subscribeEmailNotification(@AuthenticationPrincipal MemberPrincipal principal,
		@Valid @RequestBody EmailDto emailDto) {

		SubscribeEmailNotificationCommand command =
			new SubscribeEmailNotificationCommand(principal.getMemberId(), new Email(emailDto.getEmail()));

		subscribeEmailNotificationUseCase.subscribeEmailNotification(command);

		return ResponseEntity.ok("신청 완료");
	}

	@GetMapping("/status")
	public ResponseEntity<EmailNotificationStatus> findEmailNotificationStatus(@AuthenticationPrincipal MemberPrincipal principal) {
		EmailNotificationStatus emailNotificationStatus = findEmailNotificationStatusQuery.findStatus(principal.getMemberId());
		return ResponseEntity.ok(emailNotificationStatus);
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateEmailNotification(@AuthenticationPrincipal MemberPrincipal principal,
		@Valid @RequestBody EmailDto dto) {
		changeNotificationEmailUseCase.changeNotificationEmail(principal.getMemberId(), new Email(dto.getEmail()));
		return ResponseEntity.ok("수정 완료");
	}

	@DeleteMapping("/unsubscribe")
	public ResponseEntity<String> unsubscribeEmailNotification(@AuthenticationPrincipal MemberPrincipal principal) {
		unsubscribeEmailNotificationUseCase.unsubscribeEmailNotification(principal.getMemberId());
		return ResponseEntity.ok("해지 완료");
	}

	@PostMapping("/criteria/add")
	public ResponseEntity<String> addNotificationCriterion(@AuthenticationPrincipal MemberPrincipal principal,
		@Valid @RequestBody RegisterNotificationCriterionWebRequest dto) {

		RegisterNotificationCriterionCommand command = new RegisterNotificationCriterionCommand(
			principal.getMemberId(),
			dto.getCompetitionRate(),
			dto.getLockupRate());

		registerNotificationCriterionUseCase.registerNotificationCriterion(command);

		return ResponseEntity.ok().body("알림 기준 추가 완료");
	}

	@GetMapping("/criteria/list")
	public ResponseEntity<List<NotificationCriterionView>> findNotificationCriteria(
		@AuthenticationPrincipal MemberPrincipal principal) {

		List<NotificationCriterionView> memberNotificationCriteria =
			findMemberNotificationCriteriaQuery.findMemberNotificationCriteria(principal.getMemberId());

		return ResponseEntity.ok().body(memberNotificationCriteria);
	}

	@PutMapping("/criteria/update")
	public ResponseEntity<String> updateNotificationCriterion(@AuthenticationPrincipal MemberPrincipal principal,
		@Valid @RequestBody UpdateNotificationCriterionWebRequest dto) {

		UpdateNotificationCriterionCommand command = new UpdateNotificationCriterionCommand(principal.getMemberId(),
			Id.fromString(dto.getId()), dto.getCompetitionRate(), dto.getLockupRate());

		updateNotificationCriterionUseCase.updateNotificationCriterion(command);

		return ResponseEntity.ok().body("알림 기준 수정 완료");
	}

	@DeleteMapping("/criteria/delete")
	public ResponseEntity<String> deleteNotificationCriterion(@AuthenticationPrincipal MemberPrincipal principal,
		@RequestParam("id") String id) {

		DeleteNotificationCriterionCommand command = new DeleteNotificationCriterionCommand(principal.getMemberId(),
			Id.fromString(id));

		deleteNotificationCriterionUseCase.deleteNotificationCriterion(command);

		return ResponseEntity.ok().body("알림 기준 삭제 완료");
	}
}
