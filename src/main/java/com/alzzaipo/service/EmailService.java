package com.alzzaipo.service;

import com.alzzaipo.domain.emailVerification.EmailVerification;
import com.alzzaipo.domain.emailVerification.EmailVerificationRepository;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class EmailService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final JavaMailSender javaMailSender;
    private final MemberService memberService;

    // 사용자의 이메일로 인증코드 전송
    public void sendAuthCode(String email) {
        // 인증코드 생성
        String authCode = createAuthCode();

        // 이메일 내용 작성
        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        simpleMessage.setFrom("alzzaipo@daum.net");
        simpleMessage.setTo(email);
        simpleMessage.setSubject("[알짜공모주] 이메일 인증코드");
        simpleMessage.setText("인증코드 : " + authCode);

        try {
            // (이메일, 인증코드)정보 데이터베이스에 저장
            emailVerificationRepository.save(new EmailVerification(email, authCode));

            // 이메일 전송
            javaMailSender.send(simpleMessage);

        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.AUTHENTICATION_CODE_SEND_FAILED, "인증코드 전송 실패");
        }
    }

    // 영어 소문자, 대문자, 숫자로 구성된 랜덤한 인증코드 반환
    public String createAuthCode() {
        Random random = new Random();
        StringBuffer authCode = new StringBuffer();

        for(int i=0;i<8;i++) {
            // 0~2의 숫자 중 하나를 임의로 선택
            int index = random.nextInt(3);

            switch (index) {
                case 0 :
                    // 0인 경우, 임의의 소문자를 인증코드에 추가
                    authCode.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    // 1인 경우, 임의의 대문자를 인증코드에 추가
                    authCode.append((char) (random.nextInt(26) + 65));
                    break;
                case 2:
                    // 2인 경우, 0~9 중 임의의 숫자를 인증코드에 추가
                    authCode.append(random.nextInt(9));
                    break;
            }
        }

        // 완성된 인증코드를 반환
        return authCode.toString();
    }

    public void verifyAuthCode(String email, String authCode) {
        // (이메일, 인증코드) 정보가 저장된 데이터베이스 테이블을 이메일로 조회하여 인증코드를 받아옴
        String correctAuthCode = emailVerificationRepository.findAuthCodeByEmail(email)
                // 해당 이메일과 일치하는 레코드가 없는 경우, NOT FOUND(404) 응답
                .orElseThrow(() -> new AppException(ErrorCode.EXPIRED_EMAIL_AUTHENTICATION_CODE, "해당하는 이메일이 없습니다"));

        // 인증코드가 일치하지 않는 경우, UNAUTHORIZED(401) 응답
        if(!authCode.equals(correctAuthCode)) {
            throw new AppException(ErrorCode.INCORRECT_EMAIL_AUTHENTICATION_CODE, "인증코드가 일치하지 않습니다");
        }
    }

    public void verifyEmail(String email) {
        /* 이메일 형식 검사 ('local-part@domain') */
        // 'xxx@xxxxx.xxx' 형태, 최대 256자, local-part 최대 64자, 특수문자 제한, Top-Level Domain에는 문자만 입력가능
        String pattern = "^(?=.{1,256}$)[a-zA-Z0-9._%+-]{1,64}@[a-zA-Z0-9._-]+\\.[a-zA-Z]{2,}$";
        if(!Pattern.matches(pattern, email))
            throw new AppException(ErrorCode.INVALID_EMAIL_ADDRESS_FORMAT, "이메일 형식이 올바르지 않습니다.");

        String[] parts = email.split("@");
        String localPart = parts[0];
        String domainPart = parts[1];

        // '.'으로 시작하거나 끝나는 local-part 제한, 연속된 '.' 제한
        if(localPart.startsWith(".") || localPart.endsWith(".") || localPart.contains(".."))
            throw new AppException(ErrorCode.INVALID_EMAIL_ADDRESS_FORMAT, "이메일 형식이 올바르지 않습니다");

        // '-', '_', '.' 으로 시작하는 domain 제한, 해당 특수문자로 끝나는 이메일은 정규표현식에서 검사
        if(domainPart.startsWith("-") || domainPart.startsWith("_") || domainPart.startsWith("."))
            throw new AppException(ErrorCode.INVALID_EMAIL_ADDRESS_FORMAT, "이메일 형식이 올바르지 않습니다.");


        /* 이메일 중복 검사 */
        // Member 테이블에 이미 가입된 이메일이 존재하는 경우, CONFLICT 409 응답
        memberService.findMemberByEmail(email)
                .ifPresent(member -> {
                    throw new AppException(ErrorCode.EMAIL_DUPLICATED, "이미 존재하는 이메일 입니다.");
                });
    }
}