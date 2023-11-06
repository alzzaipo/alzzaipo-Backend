package com.alzzaipo.controller;

import com.alzzaipo.config.MemberPrincipal;
import com.alzzaipo.dto.email.EmailDto;
import com.alzzaipo.dto.notification.EmailNotificationStatusResponseDto;
import com.alzzaipo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/notification")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @DeleteMapping("/criteria/delete")
    public ResponseEntity<Void> deleteNotificationCriteria(@AuthenticationPrincipal MemberPrincipal memberInfo,
                                                           @RequestParam("id") Long notificationCriteriaId) {
        notificationService.deleteNotificationCriteria(memberInfo, notificationCriteriaId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/email/status")
    public ResponseEntity<EmailNotificationStatusResponseDto> getEmailNotificationStatus(@AuthenticationPrincipal MemberPrincipal memberInfo) {
        EmailNotificationStatusResponseDto dto = notificationService.getEmailNotificationStatusResponseDto(memberInfo);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/email/subscribe")
    public ResponseEntity<Void> subscribeEmailNotification(@AuthenticationPrincipal MemberPrincipal memberInfo,
                                                           @RequestBody EmailDto dto) {
        notificationService.subscribeEmailNotification(memberInfo, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/email/update")
    public ResponseEntity<Void> updateEmailNotification(@AuthenticationPrincipal MemberPrincipal memberInfo,
                                                        @RequestBody EmailDto dto) {
        notificationService.updateEmailNotification(memberInfo, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/email/unsubscribe")
    public ResponseEntity<Void> unsubscribeEmailNotification(@AuthenticationPrincipal MemberPrincipal memberInfo) {
        notificationService.unsubscribeEmailNotification(memberInfo);
        return ResponseEntity.ok().build();
    }

}
