package com.alzzaipo.notification.adapter.in.web.criterion;

import com.alzzaipo.common.MemberPrincipal;
import com.alzzaipo.common.TsidUtil;
import com.alzzaipo.common.Uid;
import com.alzzaipo.notification.adapter.in.web.criterion.dto.RegisterNotificationCriterionWebRequest;
import com.alzzaipo.notification.adapter.in.web.criterion.dto.UpdateNotificationCriterionWebRequest;
import com.alzzaipo.notification.application.port.dto.DeleteNotificationCriterionCommand;
import com.alzzaipo.notification.application.port.dto.NotificationCriterionView;
import com.alzzaipo.notification.application.port.dto.RegisterNotificationCriterionCommand;
import com.alzzaipo.notification.application.port.dto.UpdateNotificationCriterionCommand;
import com.alzzaipo.notification.application.port.in.criterion.DeleteNotificationCriterionUseCase;
import com.alzzaipo.notification.application.port.in.criterion.FindMemberNotificationCriteriaQuery;
import com.alzzaipo.notification.application.port.in.criterion.RegisterNotificationCriterionUseCase;
import com.alzzaipo.notification.application.port.in.criterion.UpdateNotificationCriterionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification/criteria")
@RequiredArgsConstructor
public class NotificationCriterionController {

    private final RegisterNotificationCriterionUseCase registerNotificationCriterionUseCase;
    private final FindMemberNotificationCriteriaQuery findMemberNotificationCriteriaQuery;
    private final UpdateNotificationCriterionUseCase updateNotificationCriterionUseCase;
    private final DeleteNotificationCriterionUseCase deleteNotificationCriterionUseCase;

    @PostMapping("add")
    public ResponseEntity<String> addNotificationCriterion(@AuthenticationPrincipal MemberPrincipal principal,
                                                           @RequestBody RegisterNotificationCriterionWebRequest dto) {
        RegisterNotificationCriterionCommand command = new RegisterNotificationCriterionCommand(
                principal.getMemberUID(),
                dto.getCompetitionRate(),
                dto.getLockupRate());

        registerNotificationCriterionUseCase.registerNotificationCriterion(command);

        return ResponseEntity.ok().body("알림 기준 추가 완료");
    }

    @GetMapping("/list")
    public ResponseEntity<List<NotificationCriterionView>> findMemberNotificationCriteria(
            @AuthenticationPrincipal MemberPrincipal principal) {

        List<NotificationCriterionView> memberNotificationCriteria
                = findMemberNotificationCriteriaQuery.findMemberNotificationCriteria(principal.getMemberUID());

        return ResponseEntity.ok().body(memberNotificationCriteria);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateNotificationCriterion(@AuthenticationPrincipal MemberPrincipal principal,
                                                              @RequestBody UpdateNotificationCriterionWebRequest dto) {
        UpdateNotificationCriterionCommand command = new UpdateNotificationCriterionCommand(
                principal.getMemberUID(),
                new Uid(TsidUtil.toLong(dto.getUid())),
                dto.getCompetitionRate(),
                dto.getLockupRate());

        updateNotificationCriterionUseCase.updateNotificationCriterion(command);

        return ResponseEntity.ok().body("알림 기준 수정 완료");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteNotificationCriterion(@AuthenticationPrincipal MemberPrincipal principal,
                                                              @RequestParam("uid") String uid) {
        DeleteNotificationCriterionCommand command = new DeleteNotificationCriterionCommand(
                principal.getMemberUID(),
                new Uid(TsidUtil.toLong(uid)));

        deleteNotificationCriterionUseCase.deleteNotificationCriterion(command);

        return ResponseEntity.ok().body("알림 기준 삭제 완료");
    }
}
