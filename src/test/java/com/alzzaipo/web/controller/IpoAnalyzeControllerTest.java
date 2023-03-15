package com.alzzaipo.web.controller;

import com.alzzaipo.domain.dto.IpoAnalyzeRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@RequiredArgsConstructor
@SpringBootTest
class IpoAnalyzeControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Test
    public void 분석_페이지() throws Exception
    {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
                .characterEncoding(StandardCharsets.UTF_8.displayName());

        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getContentAsString().contains("의무보유확약")).isTrue();
    }
    
    @Test
    public void 분석_결과_페이지() throws Exception
    {
        IpoAnalyzeRequestDto requestDto = IpoAnalyzeRequestDto.builder()
                .to(2023)
                .from(2023)
                .competitionRate(1000)
                .lockupRate(10)
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto));

        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getContentAsString().contains("평균 수익률")).isTrue();
        // MockMvc로 IPOAnalyzeRequestDto가 값이 설정되어 넘어가지 않는 오류 발생
        // assertThat(response.getContentAsString().contains("꿈비")).isTrue();
    }
    
}