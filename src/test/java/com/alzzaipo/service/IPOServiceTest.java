package com.alzzaipo.service;

import com.alzzaipo.web.domain.IPO.IPO;
import com.alzzaipo.web.dto.IPOAnalyzeRequestDto;
import com.alzzaipo.web.dto.IPOAnalyzeResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class IPOServiceTest {

    @Autowired
    IPOService ipoService;

    @Test
    public void 종목코드_중복_처리() throws Exception
    {
        //given
        int stockCode = 12345;

        IPO ipo1 = IPO.builder()
                .stockName("회사1")
                .stockCode(stockCode)
                .build();

        IPO ipo2 = IPO.builder()
                .stockName("회사2")
                .stockCode(stockCode)
                .build();

        ipoService.save(ipo1);

        //when
        ipoService.save(ipo2);

        //then
        assertThat(ipoService.findByStockCode(stockCode).getStockName()).isSameAs(ipo1.getStockName());
    }

    @Test
    public void analyze() throws Exception
    {
        //given
        IPO ipo1 = IPO.builder()
                .stockName("stock1")
                .stockCode(123)
                .listedDate(LocalDate.of(2022, 2, 4))
                .competitionRate(1000)
                .lockupRate(10)
                .build();

        IPO ipo2 = IPO.builder()
                .stockName("stock2")
                .stockCode(124)
                .listedDate(LocalDate.of(2023, 2, 4))
                .competitionRate(1000)
                .lockupRate(20)
                .build();

        IPO ipo3 = IPO.builder()
                .stockName("stock3")
                .stockCode(125)
                .listedDate(LocalDate.of(2023, 2, 10))
                .competitionRate(5)
                .lockupRate(1)
                .build();

        ipoService.save(ipo1);
        ipoService.save(ipo2);
        ipoService.save(ipo3);

        IPOAnalyzeRequestDto requestDto = IPOAnalyzeRequestDto.builder()
                .from(2023)
                .to(2023)
                .competitionRate(100)
                .lockupRate(10)
                .build();

        //when
        List<IPOAnalyzeResponseDto> result = ipoService.analyze(requestDto);

        //then
        assertThat(result.size()).isSameAs(1);
        assertThat(result.get(0).getStockCode()).isSameAs(ipo2.getStockCode());
    }

}