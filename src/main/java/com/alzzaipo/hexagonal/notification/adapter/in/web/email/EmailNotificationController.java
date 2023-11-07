package com.alzzaipo.hexagonal.notification.adapter.in.web.email;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.common.MemberPrincipal;
import com.alzzaipo.hexagonal.notification.adapter.in.web.email.dto.EmailDto;
import com.alzzaipo.hexagonal.notification.application.port.in.email.SubscribeEmailNotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification/email")
@RequiredArgsConstructor
public class EmailNotificationController {

    private final SubscribeEmailNotificationUseCase subscribeEmailNotificationUseCase;

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribeEmailNotification(@AuthenticationPrincipal MemberPrincipal principal,
                                                             @RequestBody EmailDto dto) {

        subscribeEmailNotificationUseCase.subscribeEmailNotification(
                principal.getMemberUID(),
                new Email(dto.getEmail()));

        return ResponseEntity.ok().body("이메일 알림 신청 완료");
    }

}
