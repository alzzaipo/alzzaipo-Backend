package com.alzzaipo.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class IpoAnalyzeResponseDto {

    //종목명
    private String stockName;

    // 종목 코드
    private int stockCode;

    // 희망공모가 하단(원)
    private int expectedOfferingPriceMin;

    // 희망공모가 상단(원)
    private int expectedOfferingPriceMax;

    // 최종공모가(원)
    private int fixedOfferingPrice;

    // 공모금액(원)
    private int totalAmount;

    // 기관경쟁률
    private int competitionRate;

    // 의무보유확약비율
    private int lockupRate;

    // 청약 주관사
    private String agents;

    // 상장일
    private LocalDate listedDate;

    // 상장일 시초가
    private int initialMarketPrice;

    // 수익률
    private int profitRate;


    @Builder
    public IpoAnalyzeResponseDto(String stockName, int stockCode, int expectedOfferingPriceMin, int expectedOfferingPriceMax,
                                 int fixedOfferingPrice, int totalAmount, int competitionRate, int lockupRate,
                                 String agents, LocalDate listedDate, int initialMarketPrice, int profitRate) {
        this.stockName = stockName;
        this.stockCode = stockCode;
        this.expectedOfferingPriceMin = expectedOfferingPriceMin;
        this.expectedOfferingPriceMax = expectedOfferingPriceMax;
        this.fixedOfferingPrice = fixedOfferingPrice;
        this.totalAmount = totalAmount;
        this.competitionRate = competitionRate;
        this.lockupRate = lockupRate;
        this.agents = agents;
        this.listedDate = listedDate;
        this.initialMarketPrice = initialMarketPrice;
        this.profitRate = profitRate;
    }
}
