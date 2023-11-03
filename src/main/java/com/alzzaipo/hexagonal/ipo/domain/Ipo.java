package com.alzzaipo.hexagonal.ipo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Ipo {

    // 종목명
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

    // 청약 시작일
    private LocalDate subscribeStartDate;

    // 청약 종료일
    private LocalDate subscribeEndDate;

    // 상장일
    private LocalDate listedDate;

    // 상장일 시초가
    private int initialMarketPrice;

    // 수익률
    private int profitRate;

}
