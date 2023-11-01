package com.alzzaipo.controller;

import com.alzzaipo.dto.ipo.IpoAnalyzeRequestDto;
import com.alzzaipo.dto.ipo.IpoAnalyzeResponseDto;
import com.alzzaipo.dto.ipo.IpoDto;
import com.alzzaipo.dto.ipo.IpoListDto;
import com.alzzaipo.service.IpoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class IpoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    IpoService ipoService;

    @Test
    @DisplayName("공모주 목록 조회 - JSON 포맷 테스트")
    public void 공모주_목록_조회() throws Exception {
        //given
        List<IpoListDto> ipoList = new ArrayList<>();
        ipoList.add(new IpoListDto("stock1", 111111));
        ipoList.add(new IpoListDto("stock2", 222222));
        when(ipoService.getIpoList()).thenReturn(ipoList);

        //when, then
        mockMvc.perform(get("/ipo/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].stockName").value("stock1"))
                .andExpect(jsonPath("$[0].stockCode").value("111111"))
                .andExpect(jsonPath("$[1].stockName").value("stock2"))
                .andExpect(jsonPath("$[1].stockCode").value("222222"));
    }
    
    @Test
    @DisplayName("공모주 분석 결과 - JSON 포맷 테스트")
    public void 공모주_분석_성공() throws Exception {
        //given
        IpoDto ipo1 = IpoDto.builder()
                .stockName("stock1")
                .stockCode(111111)
                .profitRate(11)
                .build();

        IpoDto ipo2 = IpoDto.builder()
                .stockName("stock2")
                .stockCode(222222)
                .profitRate(22)
                .build();

        IpoDto ipo3 = IpoDto.builder()
                .stockName("stock3")
                .stockCode(333333)
                .profitRate(33)
                .build();

        List<IpoDto> ipoDtoList = new ArrayList<>();
        ipoDtoList.add(ipo1);
        ipoDtoList.add(ipo2);
        ipoDtoList.add(ipo3);

        int averageProfitRate = 22;

        IpoAnalyzeResponseDto responseDto = new IpoAnalyzeResponseDto(averageProfitRate, ipoDtoList);

        when(ipoService.analyze(any()))
                .thenReturn(responseDto);

        //when, then
        IpoAnalyzeRequestDto requestDto
                = new IpoAnalyzeRequestDto(2020, 2023, 1000, 10);

        mockMvc.perform(post("/ipo/analyze")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.averageProfitRate").exists())
                .andExpect(jsonPath("$.averageProfitRate").value(22))
                .andExpect(jsonPath("$.ipoDtoList").exists())
                .andExpect(jsonPath("$.ipoDtoList", hasSize(3)))
                .andExpect(jsonPath("$.ipoDtoList[0].stockName").value("stock1"))
                .andExpect(jsonPath("$.ipoDtoList[1].stockName").value("stock2"))
                .andExpect(jsonPath("$.ipoDtoList[2].stockName").value("stock3"));
    }

}