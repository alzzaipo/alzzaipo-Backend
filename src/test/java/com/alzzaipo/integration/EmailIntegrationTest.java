package com.alzzaipo.integration;

import com.alzzaipo.dto.email.EmailVerificationRequestDto;
import com.alzzaipo.domain.emailVerification.EmailVerification;
import com.alzzaipo.domain.emailVerification.EmailVerificationRepository;
import com.alzzaipo.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class EmailIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Transactional
    @Test
    @DisplayName("이메일 인증코드 검증 - 성공")
    public void 이메일_인증코드_검증_성공() throws Exception {
        // given : 사용자의 이메일로 인증코드를 전송한 상황에서
        String email = "test@spring.com";
        String verificationCode = emailService.createVerificationCode();

        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .verificationCode(verificationCode)
                .build();
        emailVerificationRepository.save(emailVerification);

        // when : 전송한 이메일과 인증코드가 일치하는 경우
        EmailVerificationRequestDto dto = new EmailVerificationRequestDto(email, verificationCode);

        // then : 인증은 성공한다
        mockMvc.perform(post("/api/member/validate-verification-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @DisplayName("이메일 인증코드 검증 - 실패 : 유효시간 만료")
    public void 이메일_인증코드_검증_실패1() throws Exception {
        // given : 사용자의 이메일로 인증코드를 전송한 상황에서
        String email = "test@spring.com";
        String verificationCode = emailService.createVerificationCode();

        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .verificationCode(verificationCode)
                .build();
        emailVerificationRepository.save(emailVerification);

        // when : 유효시간이 경과되어 (이메일, 인증코드) 레코드가 삭제됐을 때
        emailVerificationRepository.delete(emailVerification);

        // then : 올바른 인증정보를 입력해도 인증은 실패한다
        EmailVerificationRequestDto dto = new EmailVerificationRequestDto(email, verificationCode);

        mockMvc.perform(post("/api/member/validate-verification-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Transactional
    @Test
    @DisplayName("이메일 인증코드 검증 - 실패 : 잘못된 인증코드")
    public void 이메일_인증코드_검증_실패2() throws Exception {
        // given : 사용자의 이메일로 인증코드를 전송한 상황에서
        String email = "test@spring.com";
        String verificationCode = emailService.createVerificationCode();

        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .verificationCode(verificationCode)
                .build();
        emailVerificationRepository.save(emailVerification);

        // when : 수신받은 정보가 이메일은 일치하지만, 인증코드가 다른 경우
        String incorrectVerificationCode = "incorrect";
        EmailVerificationRequestDto dto = new EmailVerificationRequestDto(email, incorrectVerificationCode);

        // then : 인증은 실패한다
        mockMvc.perform(post("/api/member/validate-verification-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Transactional
    @Test
    @DisplayName("이메일 인증코드 검증 - 실패 : 다른 유저의 인증코드 전송")
    public void 이메일_인증코드_검증_실패3() throws Exception {
        // given : 여러 사용자에게 인증코드를 전송한 상황에서
        String emailA = "userA123@spring.com";
        String verificationCodeA = emailService.createVerificationCode();

        String emailB = "userB412@spring.com";
        String verificationCodeB = emailService.createVerificationCode();

        EmailVerification emailVerificationA = EmailVerification.builder()
                .email(emailA)
                .verificationCode(verificationCodeA)
                .build();

        EmailVerification emailVerificationB = EmailVerification.builder()
                .email(emailB)
                .verificationCode(verificationCodeB)
                .build();

        emailVerificationRepository.save(emailVerificationA);
        emailVerificationRepository.save(emailVerificationB);

        // when : 한 사용자가 다른 사용자의 인증코드를 입력해도
        EmailVerificationRequestDto dto = new EmailVerificationRequestDto(emailA, verificationCodeB);

        // then : 인증은 실패한다
        mockMvc.perform(post("/api/member/validate-verification-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Transactional
    @Test
    @DisplayName("이메일 인증코드 검증 - 실패 : 잘못된 이메일 전송")
    public void 이메일_인증코드_검증_실패4() throws Exception {
        // given : 사용자에게 인증코드를 전송한 상황에서
        String email = "test@spring.com";
        String verificationCode = emailService.createVerificationCode();

        EmailVerification emailVerificationA = EmailVerification.builder()
                .email(email)
                .verificationCode(verificationCode)
                .build();

        emailVerificationRepository.save(emailVerificationA);

        // when : 인증코드는 맞지만, 사용자의 이메일 주소가 잘못 전송됐을 때
        String incorrectEmail = "test@spring.co";
        EmailVerificationRequestDto dto = new EmailVerificationRequestDto(incorrectEmail, verificationCode);

        // then : 인증은 실패한다
        mockMvc.perform(post("/api/member/validate-verification-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Transactional
    @Test
    @DisplayName("이메일 인증코드 검증 - 실패 : 이메일 미입력")
    public void 이메일_인증코드_검증_실패5() throws Exception {
        // given : 사용자에게 인증코드를 전송한 상황에서
        String email = "test@spring.com";
        String verificationCode = emailService.createVerificationCode();

        EmailVerification emailVerificationA = EmailVerification.builder()
                .email(email)
                .verificationCode(verificationCode)
                .build();

        emailVerificationRepository.save(emailVerificationA);

        // when : 이메일 주소가 누락됐을 때
        String emptyEmail = "";
        EmailVerificationRequestDto dto = new EmailVerificationRequestDto(emptyEmail, verificationCode);

        // then : 인증은 실패한다
        mockMvc.perform(post("/api/member/validate-verification-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isBadRequest());
    }

    @Transactional
    @Test
    @DisplayName("이메일 인증코드 검증 - 실패 : 인증코드 미입력")
    public void 이메일_인증코드_검증_실패6() throws Exception {
        // given : 사용자에게 인증코드를 전송한 상황에서
        String email = "test@spring.com";
        String verificationCode = emailService.createVerificationCode();

        EmailVerification emailVerificationA = EmailVerification.builder()
                .email(email)
                .verificationCode(verificationCode)
                .build();

        emailVerificationRepository.save(emailVerificationA);

        // when : 사용자가 인증코드를 입력하지 않은 경우
        String emptyVerificationCode = "";
        EmailVerificationRequestDto dto = new EmailVerificationRequestDto(email, emptyVerificationCode);

        // then : 인증은 실패한다
        mockMvc.perform(post("/api/member/validate-verification-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isUnauthorized());
    }
}
