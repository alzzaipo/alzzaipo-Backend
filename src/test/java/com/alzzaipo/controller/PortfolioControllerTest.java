package com.alzzaipo.controller;

import com.alzzaipo.domain.dto.PortfolioCreateRequestDto;
import com.alzzaipo.domain.dto.PortfolioUpdateRequestDto;
import com.alzzaipo.domain.portfolio.Portfolio;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.exception.ErrorCode;
import com.alzzaipo.service.PortfolioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WithMockUser
@AutoConfigureMockMvc
@SpringBootTest
class PortfolioControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PortfolioService portfolioService;

    @Test
    @DisplayName("포트폴리오 생성 성공")
    public void 포트폴리오_생성_성공() throws Exception {

        when(portfolioService.create(any(), any()))
                .thenReturn(new Portfolio());

        mockMvc.perform(post("/api/portfolio/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PortfolioCreateRequestDto())))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("포트폴리오 생성 실패 - 존재하지 않는 회원 ID")
    public void 포트폴리오_생성_실패1() throws Exception {

        when(portfolioService.create(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_MEMBER_ID, "회원 조회 실패"));

        mockMvc.perform(post("/api/portfolio/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PortfolioCreateRequestDto())))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("포트폴리오 생성 실패 - 존재하지 않는 종목코드")
    public void 포트폴리오_생성_실패2() throws Exception {

        when(portfolioService.create(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_STOCKCODE, "종목코드 조회 실패"));

        mockMvc.perform(post("/api/portfolio/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PortfolioCreateRequestDto())))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("포트폴리오 수정 성공")
    public void 포트폴리오_수정_성공() throws Exception {

        when(portfolioService.update(any(), any()))
                .thenReturn(new Portfolio());

        mockMvc.perform(put("/api/portfolio/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PortfolioUpdateRequestDto())))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("포트폴리오 수정 실패 - 존재하지 않는 회원 ID")
    public void 포트폴리오_수정_실패1() throws Exception {

        when(portfolioService.update(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_MEMBER_ID, "회원 조회 실패"));

        mockMvc.perform(put("/api/portfolio/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PortfolioUpdateRequestDto())))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("포트폴리오 수정 실패 - 존재하지 않는 포트폴리오 ID")
    public void 포트폴리오_수정_실패2() throws Exception {

        when(portfolioService.update(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PORTFOLIO, "포트폴리오 조회 실패"));

        mockMvc.perform(put("/api/portfolio/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PortfolioUpdateRequestDto())))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("포트폴리오 수정 실패 - 존재하지 않는 종목코드")
    public void 포트폴리오_수정_실패3() throws Exception {

        when(portfolioService.update(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_STOCKCODE, "종목코드 조회 실패"));

        mockMvc.perform(put("/api/portfolio/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PortfolioUpdateRequestDto())))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("포트폴리오 수정 실패 - 다른 회원의 포트폴리오 수정 시도")
    public void 포트폴리오_수정_실패4() throws Exception {

        when(portfolioService.update(any(), any()))
                .thenThrow(new AppException(ErrorCode.UNAUTHORIZED, "해당 포트폴리오에 대한 권한이 없습니다."));

        mockMvc.perform(put("/api/portfolio/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PortfolioUpdateRequestDto())))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("포트폴리오 삭제 성공")
    public void 포트폴리오_삭제_성공() throws Exception {

        when(portfolioService.delete(any(), any()))
                .thenReturn(true);

        mockMvc.perform(delete("/api/portfolio/delete")
                        .param("portfolioId", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("포트폴리오 삭제 실패 - 존재하지 않는 계정 ID")
    public void 포트폴리오_삭제_실패1() throws Exception {

        when(portfolioService.delete(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_MEMBER_ID, "회원 조회 실패"));

        mockMvc.perform(delete("/api/portfolio/delete")
                        .param("portfolioId", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("포트폴리오 삭제 실패 - 존재하지 않는 포트폴리오 ID")
    public void 포트폴리오_삭제_실패2() throws Exception {

        when(portfolioService.delete(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PORTFOLIO, "포트폴리오 조회 실패"));

        mockMvc.perform(delete("/api/portfolio/delete")
                        .param("portfolioId", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("포트폴리오 삭제 실패 - 다른 회원의 포트폴리오 삭제")
    public void 포트폴리오_삭제_실패3() throws Exception {

        when(portfolioService.delete(any(), any()))
                .thenThrow(new AppException(ErrorCode.UNAUTHORIZED, "해당 포트폴리오에 대한 권한이 없습니다."));

        mockMvc.perform(delete("/api/portfolio/delete")
                        .param("portfolioId", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

}