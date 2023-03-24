package com.alzzaipo.controller;

import com.alzzaipo.domain.dto.EmailDto;
import com.alzzaipo.domain.dto.EmailVerificationRequestDto;
import com.alzzaipo.domain.emailVerification.EmailVerification;
import com.alzzaipo.domain.emailVerification.EmailVerificationRepository;
import com.alzzaipo.domain.member.Member;
import com.alzzaipo.service.EmailService;
import com.alzzaipo.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureMockMvc
@SpringBootTest
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Test
    @DisplayName("이메일 검증 - 성공")
    public void 이메일_검증_성공() throws Exception {
        String email = "test@naver.com";

        mockMvc.perform(post("/api/email/verify-email")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : local-part가 '.'으로 시작하는 경우 실패한다")
    public void 이메일_검증_실패1() throws Exception {
        String email = ".test@naver.com";

        mockMvc.perform(post("/api/email/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : local-part가 '.'으로 끝나는 경우 실패한다")
    public void 이메일_검증_실패2() throws Exception {
        String email = "test.@naver.com";

        mockMvc.perform(post("/api/email/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : 연속된 '.'이 포함된 경우 실패한다")
    public void 이메일_검증_실패3() throws Exception {
        String email = "te..st@naver.com";

        mockMvc.perform(post("/api/email/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : local-part가 64자 초과인 경우 실패한다")
    public void 이메일_검증_실패4() throws Exception {
        // lpcal-part 길이 : 70
        String email = "123456789-123456789-123456789-123456789-123456789-123456789-123456789-@naver.com";

        mockMvc.perform(post("/api/email/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : 전체 길이가 256자 초과인 경우 실패한다")
    public void 이메일_검증_실패5() throws Exception {
        // 이메일 전체 길이 : 261
        String email = "123456789-123456789-123456789-123456789-123456789-" +
                "@123456789-123456789-123456789-123456789-123456789-" +
                "123456789-123456789-123456789-123456789-123456789-" +
                "123456789-123456789-123456789-123456789-123456789-" +
                "123456789-123456789-123456789-123456789-123456789-" +
                "naver.com";

        mockMvc.perform(post("/api/email/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : local-part@domain 형태가 아닌경우 실패한다")
    public void 이메일_검증_실패6() throws Exception {
        String email = "aaa";

        mockMvc.perform(post("/api/email/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : 이상한 특수문자 포함된 경우 실패한다")
    public void 이메일_검증_실패7() throws Exception {
        String email = ";test@naver.com";

        mockMvc.perform(post("/api/email/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : TLD에는 알파벳만 존재해야 한다")
    public void 이메일_검증_실패8() throws Exception {
        String email = "test@naver.co1m";

        mockMvc.perform(post("/api/email/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : TLD는 한 글자가 될 수 없다")
    public void 이메일_검증_실패9() throws Exception {
        String email = "test@naver.c";

        mockMvc.perform(post("/api/email/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : 도메인이 '.xxx' 으로 끝나지 않으면 실패한다")
    public void 이메일_검증_실패10() throws Exception {
        String email = "test@naver";

        mockMvc.perform(post("/api/email/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : 도메인이 '-' 으로 시작하면 실패한다")
    public void 이메일_검증_실패11() throws Exception {
        String email = "test@-naver.com";

        mockMvc.perform(post("/api/email/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : 도메인이 '_' 으로 시작하면 실패한다")
    public void 이메일_검증_실패12() throws Exception {
        String email = "test@_naver.com";

        mockMvc.perform(post("/api/email/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : 도메인이 '.' 으로 시작하면 실패한다")
    public void 이메일_검증_실패13() throws Exception {
        String email = "test@.naver.com";

        mockMvc.perform(post("/api/email/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : 도메인에 이상한 특수문자가 포함되면 실패한다")
    public void 이메일_검증_실패14() throws Exception {
        String email = "test@n!aver.com";

        mockMvc.perform(post("/api/email/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @DisplayName("이메일 검증 - 실패 : 중복된 이메일의 경우 실패한다")
    public void 이메일_검증_실패15() throws Exception {
        // given : 동일한 이메일로 가입된 내역이 있는 상황에서
        Member member = new Member("qwe", "qwe", "qwe", "test@naver.com");
        memberService.save(member);

        // when : 해당 이메일로 인증코드를 요청하면
        String registeredEmail = member.getEmail();

        // then : 인증코드 전송 요청은 거부된다
        mockMvc.perform(post("/api/email/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(registeredEmail))))
                .andExpect(status().isConflict());
    }

    @Transactional
    @Test
    @DisplayName("이메일 인증코드 검증 - 성공")
    public void 이메일_인증코드_검증_성공() throws Exception {
        // given : 사용자의 이메일로 인증코드를 전송한 상황에서
        String email = "test@spring.com";
        String authCode = emailService.createAuthCode();

        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .authCode(authCode)
                .build();
        emailVerificationRepository.save(emailVerification);

        // when : 수신받은 이메일과 인증코드가 일치하는 경우
        EmailVerificationRequestDto dto = new EmailVerificationRequestDto(email, authCode);

        // then : 인증은 성공한다
        mockMvc.perform(post("/api/email/verify-authcode")
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
        String authCode = emailService.createAuthCode();

        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .authCode(authCode)
                .build();
        emailVerificationRepository.save(emailVerification);

        // when : 유효시간이 경과되어 (이메일, 인증코드) 레코드가 삭제됐을 때
        emailVerificationRepository.delete(emailVerification);

        // then : 수신받은 인증정보가 일치해도 인증은 실패한다
        EmailVerificationRequestDto dto = new EmailVerificationRequestDto(email, authCode);

        mockMvc.perform(post("/api/email/verify-authcode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isNotFound());
    }

    @Transactional
    @Test
    @DisplayName("이메일 인증코드 검증 - 실패 : 잘못된 인증코드")
    public void 이메일_인증코드_검증_실패2() throws Exception {
        // given : 사용자의 이메일로 인증코드를 전송한 상황에서
        String email = "test@spring.com";
        String authCode = emailService.createAuthCode();

        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .authCode(authCode)
                .build();
        emailVerificationRepository.save(emailVerification);

        // when : 수신받은 정보가 이메일은 일치하지만, 인증코드가 다른 경우
        String incorrectAuthCode = "incorrect";
        EmailVerificationRequestDto dto = new EmailVerificationRequestDto(email, incorrectAuthCode);

        // then : 인증은 실패한다
        mockMvc.perform(post("/api/email/verify-authcode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Transactional
    @Test
    @DisplayName("이메일 인증코드 검증 - 실패 : 다른 유저의 인증코드 전송")
    public void 이메일_인증코드_검증_실패3() throws Exception {
        // given : 여러 사용자에게 인증코드를 전송한 상황에서
        String emailA = "userA@spring.com";
        String authCodeA = emailService.createAuthCode();

        String emailB = "userB@spring.com";
        String authCodeB = emailService.createAuthCode();

        EmailVerification emailVerificationA = EmailVerification.builder()
                .email(emailA)
                .authCode(authCodeA)
                .build();

        EmailVerification emailVerificationB = EmailVerification.builder()
                .email(emailB)
                .authCode(authCodeB)
                .build();

        emailVerificationRepository.save(emailVerificationA);
        emailVerificationRepository.save(emailVerificationB);

        // when : 한 사용자가 인증코드를 잘못 입력했는데, 우연히 인증 대기중인 다른 유저의 인증코드를 전송했을 때
        EmailVerificationRequestDto dto = new EmailVerificationRequestDto(emailA, authCodeB);

        // then : 인증은 실패한다
        mockMvc.perform(post("/api/email/verify-authcode")
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
        String authCode = emailService.createAuthCode();

        EmailVerification emailVerificationA = EmailVerification.builder()
                .email(email)
                .authCode(authCode)
                .build();

        emailVerificationRepository.save(emailVerificationA);

        // when : 인증코드는 맞지만, 사용자의 이메일 주소가 잘못 전송됐을 때
        String incorrectEmail = "test@spring.co";
        EmailVerificationRequestDto dto = new EmailVerificationRequestDto(incorrectEmail, authCode);

        // then : 인증은 실패한다
        mockMvc.perform(post("/api/email/verify-authcode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isNotFound());
    }

    @Transactional
    @Test
    @DisplayName("이메일 인증코드 검증 - 실패 : 이메일 미입력")
    public void 이메일_인증코드_검증_실패5() throws Exception {
        // given : 사용자에게 인증코드를 전송한 상황에서
        String email = "test@spring.com";
        String authCode = emailService.createAuthCode();

        EmailVerification emailVerificationA = EmailVerification.builder()
                .email(email)
                .authCode(authCode)
                .build();

        emailVerificationRepository.save(emailVerificationA);

        // when : 이메일 주소가 누락됐을 때
        String emptyEmail = "";
        EmailVerificationRequestDto dto = new EmailVerificationRequestDto(emptyEmail, authCode);

        // then : 인증은 실패한다
        mockMvc.perform(post("/api/email/verify-authcode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isNotFound());
    }

    @Transactional
    @Test
    @DisplayName("이메일 인증코드 검증 - 실패 : 인증코드 미입력")
    public void 이메일_인증코드_검증_실패6() throws Exception {
        // given : 사용자에게 인증코드를 전송한 상황에서
        String email = "test@spring.com";
        String authCode = emailService.createAuthCode();

        EmailVerification emailVerificationA = EmailVerification.builder()
                .email(email)
                .authCode(authCode)
                .build();

        emailVerificationRepository.save(emailVerificationA);

        // when : 사용자가 인증코드를 입력하지 않은 경우
        String emptyAuthCode = "";
        EmailVerificationRequestDto dto = new EmailVerificationRequestDto(email, emptyAuthCode);

        // then : 인증은 실패한다
        mockMvc.perform(post("/api/email/verify-authcode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isUnauthorized());
    }

}