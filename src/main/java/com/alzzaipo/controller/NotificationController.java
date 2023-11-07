package com.alzzaipo.controller;

import com.alzzaipo.config.MemberPrincipal;
import com.alzzaipo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/notification")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @DeleteMapping("/email/unsubscribe")
    public ResponseEntity<Void> unsubscribeEmailNotification(@AuthenticationPrincipal MemberPrincipal memberInfo) {
        notificationService.unsubscribeEmailNotification(memberInfo);
        return ResponseEntity.ok().build();
    }

}
