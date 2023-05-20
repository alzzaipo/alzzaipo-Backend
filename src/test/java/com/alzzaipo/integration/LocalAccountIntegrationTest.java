package com.alzzaipo.integration;

import com.alzzaipo.domain.account.local.LocalAccount;
import com.alzzaipo.domain.account.local.LocalAccountRepository;
import com.alzzaipo.dto.email.EmailDto;
import com.alzzaipo.domain.member.Member;
import com.alzzaipo.domain.member.MemberType;
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
public class LocalAccountIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LocalAccountRepository localAccountRepository;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("이메일 검증 - 성공")
    public void 이메일_검증_성공() throws Exception {
        String email = "test@naver.com";

        mockMvc.perform(post("/api/member/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : local-part가 '.'으로 시작하는 경우 실패한다")
    public void 이메일_검증_실패1() throws Exception {
        String email = ".test@naver.com";

        mockMvc.perform(post("/api/member/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : local-part가 '.'으로 끝나는 경우 실패한다")
    public void 이메일_검증_실패2() throws Exception {
        String email = "test.@naver.com";

        mockMvc.perform(post("/api/member/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : 연속된 '.'이 포함된 경우 실패한다")
    public void 이메일_검증_실패3() throws Exception {
        String email = "te..st@naver.com";

        mockMvc.perform(post("/api/member/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : local-part@domain 형태가 아닌경우 실패한다")
    public void 이메일_검증_실패6() throws Exception {
        String email = "aaa";

        mockMvc.perform(post("/api/member/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : 이상한 특수문자 포함된 경우 실패한다")
    public void 이메일_검증_실패7() throws Exception {
        String email = ";test@naver.com";

        mockMvc.perform(post("/api/member/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : 도메인이 '.xxx' 으로 끝나지 않으면 실패한다")
    public void 이메일_검증_실패10() throws Exception {
        String email = "test@naver";

        mockMvc.perform(post("/api/member/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : 도메인이 '_' 으로 시작하면 실패한다")
    public void 이메일_검증_실패12() throws Exception {
        String email = "test@_naver.com";

        mockMvc.perform(post("/api/member/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : 도메인이 '.' 으로 시작하면 실패한다")
    public void 이메일_검증_실패13() throws Exception {
        String email = "test@.naver.com";

        mockMvc.perform(post("/api/member/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일 검증 - 실패 : 도메인에 이상한 특수문자가 포함되면 실패한다")
    public void 이메일_검증_실패14() throws Exception {
        String email = "test@n!aver.com";

        mockMvc.perform(post("/api/member/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(email))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @DisplayName("이메일 검증 - 실패 : 중복된 이메일의 경우 실패한다")
    public void 이메일_검증_실패15() throws Exception {
        // given : 동일한 이메일로 가입된 내역이 있는 상황에서
        Member member = memberService.createAndSave("test", MemberType.LOCAL);
        LocalAccount localAccount = LocalAccount.builder()
                .member(member)
                .accountId("test1234")
                .accountPassword("1Q2w#e$r1")
                .email("test@naver.com")
                .build();

        localAccountRepository.save(localAccount);

        // when : 해당 이메일로 인증코드를 요청하면
        String registeredEmail = "test@naver.com";

        // then : 인증코드 전송 요청은 거부된다
        mockMvc.perform(post("/api/member/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new  EmailDto(registeredEmail))))
                .andExpect(status().isConflict());
    }
}
