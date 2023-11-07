package com.alzzaipo.controller;

import com.alzzaipo.config.MemberPrincipal;
import com.alzzaipo.dto.email.EmailDto;
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
