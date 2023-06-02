package com.alzzaipo.controller;

import com.alzzaipo.config.MemberPrincipal;
import com.alzzaipo.dto.notification.NotificationCriteriaAddRequestDto;
import com.alzzaipo.dto.email.EmailDto;
import com.alzzaipo.dto.notification.NotificationCriteriaListDto;
import com.alzzaipo.dto.notification.NotificationCriteriaUpdateRequestDto;
import com.alzzaipo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/notification")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/email/subscribe")
    public ResponseEntity<Void> subscribeEmailNotification(@AuthenticationPrincipal MemberPrincipal memberInfo,
                                                             @RequestBody EmailDto dto) {
        notificationService.subscribeEmailNotification(memberInfo, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/criteria/add")
    public ResponseEntity<Void> addNotificationCriteria(@AuthenticationPrincipal MemberPrincipal memberInfo,
                                                        @RequestBody NotificationCriteriaAddRequestDto dto) {
        notificationService.addNotificationCriteria(memberInfo, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/criteria/list")
    public ResponseEntity<NotificationCriteriaListDto> getNotificationCriteriaList(@AuthenticationPrincipal MemberPrincipal memberInfo) {
        NotificationCriteriaListDto dto = notificationService.getNotificationCriteriaListDto(memberInfo);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/criteria/update")
    public ResponseEntity<Void> updateNotificationCriteria(@AuthenticationPrincipal MemberPrincipal memberInfo,
                                                           @RequestBody NotificationCriteriaUpdateRequestDto dto) {
        notificationService.updateNotificationCriteria(memberInfo, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/criteria/delete")
    public ResponseEntity<Void> deleteNotificationCriteria(@AuthenticationPrincipal MemberPrincipal memberInfo,
                                                           @RequestParam("id") Long notificationCriteriaId) {
        notificationService.deleteNotificationCriteria(memberInfo, notificationCriteriaId);
        return ResponseEntity.ok().build();
    }

}
