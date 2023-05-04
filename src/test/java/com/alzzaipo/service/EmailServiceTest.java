package com.alzzaipo.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @Test
    @DisplayName("인증코드는 8글자다")
    public void 인증코드_생성1_성공1() throws Exception {
        String verificationCode = emailService.createVerificationCode();
        assertThat(verificationCode.length()).isEqualTo(8);
    }

    @Test
    @DisplayName("인증코드는 매번 다르게 생성된다")
    public void 인증코드_생성_성공2() throws Exception {
        // 우연히 인증코드가 동일하게 생성되는 경우를 대비하여, 여러 개를 생성하여 검증
        String verificationCode1 = emailService.createVerificationCode();
        String verificationCode2 = emailService.createVerificationCode();
        String verificationCode3 = emailService.createVerificationCode();
        String verificationCode4 = emailService.createVerificationCode();
        String verificationCode5 = emailService.createVerificationCode();

        if(verificationCode1.equals(verificationCode2) && verificationCode2.equals(verificationCode3) && verificationCode3.equals(verificationCode4)
                && verificationCode4.equals(verificationCode5))
            Assertions.fail("인증코드가 모두 같음 : " + verificationCode1);
    }
}