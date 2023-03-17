package com.alzzaipo.api;

import com.alzzaipo.domain.dto.MemberLoginRequestDto;
import com.alzzaipo.domain.member.Member;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.exception.ErrorCode;
import com.alzzaipo.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@SpringBootTest
class MemberRestControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @Autowired
    public MemberRestControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    private Member getMember() {
        return Member.builder()
                .accountId("spring")
                .accountPassword("test")
                .email("1234@naver.com")
                .nickname("spring_test")
                .build();
    }

    @Test
    @DisplayName("회원가입 성공")
    public void 회원가입_성공() throws Exception {
        //given
        Member member = getMember();

        //when
        mockMvc.perform(post("/api/member/join")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(member)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        //then
    }

    @Test
    @DisplayName("회원가입 실패 - 계정 ID 중복")
    public void 회원가입_실패1() throws Exception {
        //given
        Member member = getMember();

        //when
        when(memberService.join(any(), any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.ACCOUNT_ID_DUPLICATED, "중복된 ID 입니다."));

        //then
        mockMvc.perform(post("/api/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(member)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @DisplayName("회원가입 실패 - email 중복")
    public void 회원가입_실패2() throws Exception {
        //given
        Member member = getMember();

        //when
        when(memberService.join(any(), any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.EMAIL_DUPLICATED, "중복된 email 입니다."));

        //then
        mockMvc.perform(post("/api/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(member)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @DisplayName("로그인 성공")
    public void 로그인_성공()  throws Exception{
        //given
        MemberLoginRequestDto dto = new MemberLoginRequestDto("accountId", "password");

        //when
        when(memberService.login(any(), any()))
                .thenReturn("token");

        //then
        mockMvc.perform(post("/api/member/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("로그인 실패 - 계정 아이디 불일치")
    public void 로그인_실패1() throws Exception {
        //given
        MemberLoginRequestDto dto = new MemberLoginRequestDto("accountId", "password");

        //when
        when(memberService.login(any(), any()))
                .thenThrow(new AppException(ErrorCode.ACCOUNT_ID_NOT_FOUND, "존재하지 않는 아이디 입니다."));

        //then
        mockMvc.perform(post("/api/member/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("로그인 실패 - 계정 비밀번호 불일치")
    public void 로그인_실패2() throws Exception {
        //given
        MemberLoginRequestDto dto = new MemberLoginRequestDto("accountId", "password");

        //when
        when(memberService.login(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 일치하지 않습니다."));

        //then
        mockMvc.perform(post("/api/member/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

}