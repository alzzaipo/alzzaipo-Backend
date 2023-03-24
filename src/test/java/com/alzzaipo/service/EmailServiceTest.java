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
        String authCode = emailService.createAuthCode();
        assertThat(authCode.length()).isEqualTo(8);
    }

    @Test
    @DisplayName("인증코드는 매번 다르게 생성된다")
    public void 인증코드_생성_성공2() throws Exception {
        // 우연히 인증코드가 동일하게 생성되는 경우를 대비하여, 여러 개를 생성하여 검증
        String authCode1 = emailService.createAuthCode();
        String authCode2 = emailService.createAuthCode();
        String authCode3 = emailService.createAuthCode();
        String authCode4 = emailService.createAuthCode();
        String authCode5 = emailService.createAuthCode();

        if(authCode1.equals(authCode2) && authCode2.equals(authCode3) && authCode3.equals(authCode4)
                && authCode4.equals(authCode5))
            Assertions.fail("인증코드가 모두 같음 : " + authCode1);
    }
}