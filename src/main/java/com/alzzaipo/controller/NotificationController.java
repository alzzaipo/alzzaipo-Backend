package com.alzzaipo.controller;

import com.alzzaipo.config.MemberPrincipal;
import com.alzzaipo.dto.NotificationCriteriaDto;
import com.alzzaipo.dto.email.EmailDto;
import com.alzzaipo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/notification")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/criteria/add")
    public ResponseEntity<String> addNotificationCriteria(@AuthenticationPrincipal MemberPrincipal memberInfo,
                                                          @RequestBody NotificationCriteriaDto dto) {
        notificationService.addNotificationCriteria(memberInfo, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/subscribe")
    public ResponseEntity<String> subscribeEmailNotification(@AuthenticationPrincipal MemberPrincipal memberInfo,
                                                             @RequestBody EmailDto dto) {
        notificationService.registerEmailNotification(memberInfo, dto);
        return ResponseEntity.ok().build();
    }
}
