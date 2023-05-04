package com.alzzaipo.controller;

import com.alzzaipo.domain.dto.LocalAccountLoginRequestDto;
import com.alzzaipo.domain.dto.LocalAccountRegisterRequestDto;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.exception.ErrorCode;
import com.alzzaipo.service.LocalAccountService;
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
public class LocalAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LocalAccountService localAccountService;

    @Test
    @DisplayName("회원가입 실패 - 계정 ID 중복")
    public void 회원가입_실패1() throws Exception {
        //when
        when(localAccountService.register(any()))
                .thenThrow(new AppException(ErrorCode.ACCOUNT_ID_DUPLICATED, "중복된 ID 입니다."));

        //then
        mockMvc.perform(post("/api/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LocalAccountRegisterRequestDto())))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @DisplayName("회원가입 실패 - email 중복")
    public void 회원가입_실패2() throws Exception {
        //when
        when(localAccountService.register(any()))
                .thenThrow(new AppException(ErrorCode.DUPLICATED_EMAIL, "중복된 email 입니다."));

        //then
        mockMvc.perform(post("/api/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LocalAccountRegisterRequestDto())))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @DisplayName("로그인 성공")
    public void 로그인_성공() throws Exception{
        //when
        when(localAccountService.login(any()))
                .thenReturn("token");

        //then
        mockMvc.perform(post("/api/member/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LocalAccountLoginRequestDto())))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 아이디 입력")
    public void 로그인_실패1() throws Exception {
        //when
        when(localAccountService.login(any()))
                .thenThrow(new AppException(ErrorCode.INVALID_ACCOUNT_ID, "아이디를 확인해 주세요."));

        //then
        mockMvc.perform(post("/api/member/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LocalAccountLoginRequestDto())))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호 입력")
    public void 로그인_실패2() throws Exception {
        //when
        when(localAccountService.login(any()))
                .thenThrow(new AppException(ErrorCode.UNAUTHORIZED, "비밀번호를 확인해 주세요"));

        //then
        mockMvc.perform(post("/api/member/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LocalAccountLoginRequestDto())))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
