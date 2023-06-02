package com.alzzaipo.service;

import com.alzzaipo.config.MemberPrincipal;
import com.alzzaipo.domain.member.Member;
import com.alzzaipo.domain.notification.criteria.NotificationCriteria;
import com.alzzaipo.domain.notification.criteria.NotificationCriteriaRepository;
import com.alzzaipo.domain.notification.email.EmailNotification;
import com.alzzaipo.domain.notification.email.EmailNotificationRepository;
import com.alzzaipo.dto.NotificationCriteriaDto;
import com.alzzaipo.dto.email.EmailDto;
import com.alzzaipo.enums.ErrorCode;
import com.alzzaipo.enums.LoginType;
import com.alzzaipo.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final MemberService memberService;
    private final NotificationCriteriaRepository notificationCriteriaRepository;
    private final EmailNotificationRepository emailNotificationRepository;
    private final EmailService emailService;

    public void addNotificationCriteria(MemberPrincipal memberInfo, NotificationCriteriaDto dto) {
        Member member = memberService.findById(memberInfo.getMemberId());

        List<NotificationCriteria> criteriaList = notificationCriteriaRepository.findByMemberId(member.getId());

        if(criteriaList.size() >= 3) {
            throw new AppException(ErrorCode.BAD_REQUEST, "최대 알림 기준 초과");
        }

        NotificationCriteria notificationCriteria = NotificationCriteria.builder()
                .minCompetitionRate(dto.getCompetitionRate())
                .minLockupRate(dto.getLockupRate())
                .member(member)
                .build();

        notificationCriteriaRepository.save(notificationCriteria);
    }

    public void registerEmailNotification(MemberPrincipal memberInfo, EmailDto dto) {
        Member member = memberService.findById(memberInfo.getMemberId());
        String registeredEmail = "";

        if(memberInfo.getCurrentLoginType() == LoginType.LOCAL) {
            registeredEmail = member.getLocalAccount().getEmail();
        } else {
            registeredEmail = member.getSocialAccount(memberInfo.getCurrentLoginType()).getEmail();
        }

        if(!registeredEmail.equals(dto.getEmail()) && emailService.getEmailVerificationStatus(dto.getEmail()) == false)
            throw new AppException(ErrorCode.UNAUTHORIZED, "인증되지 않은 이메일 입니다.");

        EmailNotification en = EmailNotification.builder()
                .email(dto.getEmail())
                .member(member)
                .build();

        emailNotificationRepository.save(en);
    }
}
