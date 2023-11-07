package com.alzzaipo.hexagonal.notification.adapter.in.web.email;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.common.MemberPrincipal;
import com.alzzaipo.hexagonal.notification.adapter.in.web.email.dto.EmailDto;
import com.alzzaipo.hexagonal.notification.application.port.dto.EmailNotificationStatus;
import com.alzzaipo.hexagonal.notification.application.port.in.email.FindEmailNotificationStatusQuery;
import com.alzzaipo.hexagonal.notification.application.port.in.email.SubscribeEmailNotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification/email")
@RequiredArgsConstructor
public class EmailNotificationController {

    private final SubscribeEmailNotificationUseCase subscribeEmailNotificationUseCase;
    private final FindEmailNotificationStatusQuery findEmailNotificationStatusQuery;

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribeEmailNotification(@AuthenticationPrincipal MemberPrincipal principal,
                                                             @RequestBody EmailDto dto) {

        subscribeEmailNotificationUseCase.subscribeEmailNotification(
                principal.getMemberUID(),
                new Email(dto.getEmail()));

        return ResponseEntity.ok("이메일 알림 신청 완료");
    }

    @GetMapping("/new/status")
    public ResponseEntity<EmailNotificationStatus> findEmailNotificationStatus(@AuthenticationPrincipal MemberPrincipal principal) {

        EmailNotificationStatus emailNotificationStatus
                = findEmailNotificationStatusQuery.findEmailNotificationStatus(principal.getMemberUID());

        return ResponseEntity.ok(emailNotificationStatus);
    }
}
