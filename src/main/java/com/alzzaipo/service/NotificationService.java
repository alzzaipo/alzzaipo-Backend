package com.alzzaipo.service;

import com.alzzaipo.config.MemberPrincipal;
import com.alzzaipo.domain.member.Member;
import com.alzzaipo.domain.notification.criteria.NotificationCriteria;
import com.alzzaipo.domain.notification.criteria.NotificationCriteriaRepository;
import com.alzzaipo.domain.notification.email.EmailNotification;
import com.alzzaipo.domain.notification.email.EmailNotificationRepository;
import com.alzzaipo.dto.notification.*;
import com.alzzaipo.dto.email.EmailDto;
import com.alzzaipo.enums.ErrorCode;
import com.alzzaipo.enums.LoginType;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.util.DataFormatUtil;
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
            throw new AppException(ErrorCode.BAD_REQUEST, "알림기준 입력값 오류");
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

    // 이메일 알림 구독 상태 조회
    public EmailNotificationStatusResponseDto getEmailNotificationStatusResponseDto(MemberPrincipal memberInfo) {
        Member member = memberService.findById(memberInfo.getMemberId());
        EmailNotification memberEmailNotification = member.getEmailNotification();

        EmailNotificationStatusResponseDto dto = new EmailNotificationStatusResponseDto(false, "");

        if(memberEmailNotification != null) {
            dto.setSubscriptionStatus(true);
            dto.setEmail(memberEmailNotification.getEmail());
        }

        return dto;
    }

    // 이메일 알림 구독 신청
    public void subscribeEmailNotification(MemberPrincipal memberInfo, EmailDto dto) {
        Member member = memberService.findById(memberInfo.getMemberId());
        String registeredEmail = "";

        if(memberInfo.getCurrentLoginType() == LoginType.LOCAL) {
            registeredEmail = member.getLocalAccount().getEmail();
        } else {
            registeredEmail = member.getSocialAccount(memberInfo.getCurrentLoginType()).getEmail();
        }

        // 이메일 형식 검사
        DataFormatUtil.validateEmailFormat(dto.getEmail());

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

    // 알림 구독 이메일 주소 변경
    public void updateEmailNotification(MemberPrincipal memberInfo, EmailDto dto) {
        Member member = memberService.findById(memberInfo.getMemberId());
        String registeredEmail = "";

        if(memberInfo.getCurrentLoginType() == LoginType.LOCAL) {
            registeredEmail = member.getLocalAccount().getEmail();
        } else {
            registeredEmail = member.getSocialAccount(memberInfo.getCurrentLoginType()).getEmail();
        }

        EmailNotification emailNotification = member.getEmailNotification();

        // 구독 내역 확인
        if(emailNotification == null)
            throw new AppException(ErrorCode.BAD_REQUEST, "신청 내역 없음");

        // 이메일 형식 검사
        DataFormatUtil.validateEmailFormat(dto.getEmail());

        // 이메일 중복 검사
        emailNotificationRepository.findByEmail(dto.getEmail())
                .ifPresent(en -> {
                    throw new AppException(ErrorCode.DUPLICATED_EMAIL, "이미 등록된 이메일 입니다.");
                });

        // 가입시 등록한 이메일이 아닌 경우 이메일 인증 여부 검사
        if(!registeredEmail.equals(dto.getEmail()) && emailService.getEmailVerificationStatus(dto.getEmail()) == false)
            throw new AppException(ErrorCode.UNAUTHORIZED, "인증되지 않은 이메일 입니다.");

        // 알림 이메일 변경
        emailNotification.changeEmail(dto.getEmail());
    }


    // 이메일 알림 구독 해지
    public void unsubscribeEmailNotification(MemberPrincipal memberInfo) {
        Member member = memberService.findById(memberInfo.getMemberId());

        // 구독 내역 확인
        if(member.getEmailNotification() == null) {
            throw new AppException(ErrorCode.BAD_REQUEST, "구독 내역 없음");
        }

        emailNotificationRepository.delete(member.getEmailNotification());
    }


}
