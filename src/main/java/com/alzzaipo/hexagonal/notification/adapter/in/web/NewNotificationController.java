package com.alzzaipo.hexagonal.notification.adapter.in.web;

import com.alzzaipo.hexagonal.common.MemberPrincipal;
import com.alzzaipo.hexagonal.notification.adapter.in.web.dto.RegisterNotificationCriteriaWebRequest;
import com.alzzaipo.hexagonal.notification.application.port.dto.RegisterNotificationCriteriaCommand;
import com.alzzaipo.hexagonal.notification.application.port.in.RegisterNotificationCriteriaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NewNotificationController {

    private final RegisterNotificationCriteriaUseCase registerNotificationCriteriaUseCase;

    @PostMapping("/criteria/add")
    public ResponseEntity<String> add(@AuthenticationPrincipal MemberPrincipal principal,
                                      @RequestBody RegisterNotificationCriteriaWebRequest dto) {
        RegisterNotificationCriteriaCommand command = new RegisterNotificationCriteriaCommand(
                principal.getMemberUID(),
                dto.getCompetitionRate(),
                dto.getLockupRate());

        registerNotificationCriteriaUseCase.registerNotificationCriteria(command);

        return ResponseEntity.ok().body("알림 기준 추가 완료");
    }

}
