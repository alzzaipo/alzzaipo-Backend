package com.alzzaipo.hexagonal.notification.adapter.in.web;

import com.alzzaipo.hexagonal.common.MemberPrincipal;
import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.notification.adapter.in.web.dto.RegisterNotificationCriterionWebRequest;
import com.alzzaipo.hexagonal.notification.adapter.in.web.dto.UpdateNotificationCriterionWebRequest;
import com.alzzaipo.hexagonal.notification.application.port.dto.DeleteNotificationCriterionCommand;
import com.alzzaipo.hexagonal.notification.application.port.dto.NotificationCriterionView;
import com.alzzaipo.hexagonal.notification.application.port.dto.RegisterNotificationCriterionCommand;
import com.alzzaipo.hexagonal.notification.application.port.dto.UpdateNotificationCriterionCommand;
import com.alzzaipo.hexagonal.notification.application.port.in.DeleteNotificationCriterionUseCase;
import com.alzzaipo.hexagonal.notification.application.port.in.FindMemberNotificationCriteriaQuery;
import com.alzzaipo.hexagonal.notification.application.port.in.RegisterNotificationCriterionUseCase;
import com.alzzaipo.hexagonal.notification.application.port.in.UpdateNotificationCriterionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NewNotificationController {

    private final RegisterNotificationCriterionUseCase registerNotificationCriterionUseCase;
    private final FindMemberNotificationCriteriaQuery findMemberNotificationCriteriaQuery;
    private final UpdateNotificationCriterionUseCase updateNotificationCriterionUseCase;
    private final DeleteNotificationCriterionUseCase deleteNotificationCriterionUseCase;

    @PostMapping("/criteria/add")
    public ResponseEntity<String> addNotificationCriterion(@AuthenticationPrincipal MemberPrincipal principal,
                                                           @RequestBody RegisterNotificationCriterionWebRequest dto) {
        RegisterNotificationCriterionCommand command = new RegisterNotificationCriterionCommand(
                principal.getMemberUID(),
                dto.getCompetitionRate(),
                dto.getLockupRate());

        registerNotificationCriterionUseCase.registerNotificationCriterion(command);

        return ResponseEntity.ok().body("알림 기준 추가 완료");
    }

    @GetMapping("/criteria/list")
    public ResponseEntity<List<NotificationCriterionView>> findMemberNotificationCriteria(
            @AuthenticationPrincipal MemberPrincipal principal) {

        List<NotificationCriterionView> memberNotificationCriteria
                = findMemberNotificationCriteriaQuery.findMemberNotificationCriteria(principal.getMemberUID());

        return ResponseEntity.ok().body(memberNotificationCriteria);
    }

    @PutMapping("/criteria/update")
    public ResponseEntity<String> updateNotificationCriterion(@AuthenticationPrincipal MemberPrincipal principal,
                                                              @RequestBody UpdateNotificationCriterionWebRequest dto) {
        UpdateNotificationCriterionCommand command = new UpdateNotificationCriterionCommand(
                principal.getMemberUID(),
                new Uid(dto.getUid()),
                dto.getCompetitionRate(),
                dto.getLockupRate());

        updateNotificationCriterionUseCase.updateNotificationCriterion(command);

        return ResponseEntity.ok().body("알림 기준 수정 완료");
    }

    @DeleteMapping("/criteria/delete")
    public ResponseEntity<String> deleteNotificationCriterion(@AuthenticationPrincipal MemberPrincipal principal,
                                                              @RequestParam("uid") Long notificationCriterionUID) {
        DeleteNotificationCriterionCommand command = new DeleteNotificationCriterionCommand(
                principal.getMemberUID(),
                new Uid(notificationCriterionUID));

        deleteNotificationCriterionUseCase.deleteNotificationCriterion(command);

        return ResponseEntity.ok().body("알림 기준 삭제 완료");
    }
}
