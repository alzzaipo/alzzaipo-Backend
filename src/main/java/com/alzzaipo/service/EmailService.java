package com.alzzaipo.service;

import com.alzzaipo.config.EmailUtil;
import com.alzzaipo.domain.account.local.LocalAccountRepository;
import com.alzzaipo.domain.emailVerification.EmailVerification;
import com.alzzaipo.domain.emailVerification.EmailVerificationRepository;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class EmailService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final JavaMailSender javaMailSender;
    private final LocalAccountRepository localAccountRepository;

    // 사용자의 이메일로 인증코드 전송
    @Transactional
    public void sendVerificationCode(String email) {
        // 이메일 형식 검증
        EmailUtil.verifyEmailFormat(email);

        // 이메일 중복 검사
        localAccountRepository.findByEmail(email)
                .ifPresent(localAccount -> {
                    throw new AppException(ErrorCode.DUPLICATED_EMAIL, "중복된 이메일 입니다.");
                });

        // 인증코드 생성
        String verificationCode = createVerificationCode();

        // 이메일 내용 작성
        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        simpleMessage.setFrom("alzzaipo@daum.net");
        simpleMessage.setTo(email);
        simpleMessage.setSubject("[알짜공모주] 이메일 인증코드");
        simpleMessage.setText("인증코드 : " + verificationCode);

        try {
            // 이메일 전송
            javaMailSender.send(simpleMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.AUTHENTICATION_CODE_SEND_FAILED, "인증메일 전송 실패");
        }

        // 이전에 인증코드를 요청한 내역이 있는 경우 무효 처리(삭제)
        emailVerificationRepository.findByEmail(email)
                .ifPresent(ev -> {
                    emailVerificationRepository.delete(ev);
                });

        // 인증 정보(이메일, 인증코드) 데이터베이스에 저장
        emailVerificationRepository.save(new EmailVerification(email, verificationCode));
    }

    // 영어 소문자, 대문자, 숫자로 구성된 랜덤한 인증코드 반환
    public String createVerificationCode() {
        Random random = new Random();
        StringBuffer verificationCode = new StringBuffer();

        for(int i=0;i<8;i++) {
            // 0~2의 숫자 중 하나를 임의로 선택
            int index = random.nextInt(3);

            switch (index) {
                case 0 :
                    // 0인 경우, 임의의 소문자를 인증코드에 추가
                    verificationCode.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    // 1인 경우, 임의의 대문자를 인증코드에 추가
                    verificationCode.append((char) (random.nextInt(26) + 65));
                    break;
                case 2:
                    // 2인 경우, 0~9 중 임의의 숫자를 인증코드에 추가
                    verificationCode.append(random.nextInt(9));
                    break;
            }
        }

        // 완성된 인증코드를 반환
        return verificationCode.toString();
    }

    @Transactional
    public void validateVerificationCode(String email, String verificationCode) {
        // 이메일 포맷 확인
        EmailUtil.verifyEmailFormat(email);

        // 인증코드 포맷 확인
        EmailUtil.validateVerificationCodeFormat(verificationCode);

        String correctVerificationCode = emailVerificationRepository.findVerificationCodeByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED, "요청된 내역이 없습니다."));

        // 인증코드가 일치하지 않는 경우, UNAUTHORIZED(401) 응답
        if(!verificationCode.equals(correctVerificationCode)) {
            throw new AppException(ErrorCode.INVALID_EMAIL_VERIFICATION_CODE, "잘못된 인증코드 입니다.");
        }

        emailVerificationRepository.changeVerificationStatusToVerified(email);
    }

    @Transactional(readOnly = true)
    public boolean getEmailVerificationStatus(String email) {
        Optional<EmailVerification> ev = emailVerificationRepository.findByEmail(email);
        return ev.isEmpty()? false : ev.get().isVerificationStatus();
    }


}