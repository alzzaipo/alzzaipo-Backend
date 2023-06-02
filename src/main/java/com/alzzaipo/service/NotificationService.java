package com.alzzaipo.service;

import com.alzzaipo.config.MemberPrincipal;
import com.alzzaipo.domain.member.Member;
import com.alzzaipo.domain.notification.criteria.NotificationCriteria;
import com.alzzaipo.domain.notification.criteria.NotificationCriteriaRepository;
import com.alzzaipo.domain.notification.email.EmailNotification;
import com.alzzaipo.domain.notification.email.EmailNotificationRepository;
import com.alzzaipo.dto.notification.NotificationCriteriaAddRequestDto;
import com.alzzaipo.dto.email.EmailDto;
import com.alzzaipo.dto.notification.NotificationCriteriaDto;
import com.alzzaipo.dto.notification.NotificationCriteriaListDto;
import com.alzzaipo.dto.notification.NotificationCriteriaUpdateRequestDto;
import com.alzzaipo.enums.ErrorCode;
import com.alzzaipo.enums.LoginType;
import com.alzzaipo.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final MemberService memberService;
    private final NotificationCriteriaRepository notificationCriteriaRepository;
    private final EmailNotificationRepository emailNotificationRepository;
    private final EmailService emailService;

    public void subscribeEmailNotification(MemberPrincipal memberInfo, EmailDto dto) {
        Member member = memberService.findById(memberInfo.getMemberId());
        String registeredEmail = "";

        if(memberInfo.getCurrentLoginType() == LoginType.LOCAL) {
            registeredEmail = member.getLocalAccount().getEmail();
        } else {
            registeredEmail = member.getSocialAccount(memberInfo.getCurrentLoginType()).getEmail();
        }

        // 이메일 중복 검사
        emailNotificationRepository.findByEmail(dto.getEmail())
                .ifPresent(en -> {
                    throw new AppException(ErrorCode.DUPLICATED_EMAIL, "이미 등록된 이메일 입니다.");
                });

        // 가입시 등록한 이메일이 아닌 경우 이메일 인증 요구
        if(!registeredEmail.equals(dto.getEmail()) && emailService.getEmailVerificationStatus(dto.getEmail()) == false)
            throw new AppException(ErrorCode.UNAUTHORIZED, "인증되지 않은 이메일 입니다.");

        EmailNotification en = EmailNotification.builder()
                .email(dto.getEmail())
                .member(member)
                .build();

        emailNotificationRepository.save(en);
    }

    public void addNotificationCriteria(MemberPrincipal memberInfo, NotificationCriteriaAddRequestDto dto) {
        Member member = memberService.findById(memberInfo.getMemberId());

        List<NotificationCriteria> criteriaList = member.getNotificationCriteriaList();

        if(criteriaList.size() >= 3) {
            throw new AppException(ErrorCode.BAD_REQUEST, "최대 개수 초과");
        }

        if(!(dto.getCompetitionRate() >= 0 && dto.getCompetitionRate() <= 10000 &&
                dto.getLockupRate() >= 0 && dto.getLockupRate() <= 100)) {
            throw new AppException(ErrorCode.BAD_REQUEST, "알림 기준 입력값 오류");
        }

        NotificationCriteria notificationCriteria = NotificationCriteria.builder()
                .minCompetitionRate(dto.getCompetitionRate())
                .minLockupRate(dto.getLockupRate())
                .member(member)
                .build();

        notificationCriteriaRepository.save(notificationCriteria);

        member.addNotificationCriteria(notificationCriteria);
    }

    @Transactional(readOnly = true)
    public NotificationCriteriaListDto getNotificationCriteriaListDto(MemberPrincipal memberInfo) {
        Member member = memberService.findById(memberInfo.getMemberId());

        List<NotificationCriteriaDto> dtoList = member.getNotificationCriteriaList().stream()
                .map(NotificationCriteria::toDto)
                .collect(Collectors.toList());

        NotificationCriteriaListDto notificationCriteriaListDto = new NotificationCriteriaListDto(dtoList);

        return notificationCriteriaListDto;
    }

    public void updateNotificationCriteria(MemberPrincipal memberInfo, NotificationCriteriaUpdateRequestDto dto) {
        NotificationCriteria notificationCriteria = notificationCriteriaRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "알림기준 조회 실패"));

        if(memberInfo.getMemberId() != null && memberInfo.getMemberId() != notificationCriteria.getMember().getId() ) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "권한 없음");
        }

        if(!(dto.getCompetitionRate() >= 0 && dto.getCompetitionRate() <= 5000 &&
                dto.getLockupRate() >= 0 && dto.getLockupRate() <= 100)) {
            throw new AppException(ErrorCode.BAD_REQUEST, "알림 기준 입력값 오류");
        }

        notificationCriteria.update(dto.getCompetitionRate(), dto.getLockupRate());
    }

    public void deleteNotificationCriteria(MemberPrincipal memberInfo, Long notificationCriteriaId) {
        NotificationCriteria notificationCriteria = notificationCriteriaRepository.findById(notificationCriteriaId)
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "알림기준 조회 실패"));

        if(memberInfo.getMemberId() != null && memberInfo.getMemberId() != notificationCriteria.getMember().getId() ) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "권한 없음");
        }

        notificationCriteriaRepository.delete(notificationCriteria);
    }

}
