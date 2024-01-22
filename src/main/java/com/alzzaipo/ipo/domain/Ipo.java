package com.alzzaipo.ipo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Ipo {

    // 종목명
    private final String stockName;

    // 종목 코드
    private final int stockCode;

    // 희망공모가 하단(원)
    private final int expectedOfferingPriceMin;

    // 희망공모가 상단(원)
    private final int expectedOfferingPriceMax;

    // 최종공모가(원)
    private final int fixedOfferingPrice;

    // 공모금액(원)
    private final int totalAmount;

    // 기관경쟁률
    private final int competitionRate;

    // 의무보유확약비율
    private final int lockupRate;

    // 청약 주관사
    private final String agents;

    // 청약 시작일
    private final LocalDate subscribeStartDate;

    // 청약 종료일
    private final LocalDate subscribeEndDate;

    // 상장일
    private final LocalDate listedDate;

    // 상장일 시초가
    private int initialMarketPrice;

    // 수익률
    private int profitRate;

    public void updateInitialMarketPrice(int initialMarketPrice) {
        this.initialMarketPrice = initialMarketPrice;
        this.profitRate = (int) (((double) (initialMarketPrice - fixedOfferingPrice) / fixedOfferingPrice) * 100);
    }
}
