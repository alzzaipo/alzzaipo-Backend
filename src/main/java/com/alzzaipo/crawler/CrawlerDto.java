package com.alzzaipo.crawler;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CrawlerDto {

    // 종목명
    String stockName;

    // 희망공모가 하단
    int expectedOfferingPriceMin;

    // 희망공모가 상단
    int expectedOfferingPriceMax;

    // 최종공모가
    int fixedOfferingPrice;

    // 공모금액
    int totalAmount;

    // 기관경쟁률
    int competitionRate;

    // 의무보유확약비율
    int lockupRate;

    // 주관사
    String agents;

    // 청약 시작일
    LocalDate subscribeStartDate;

    // 청약 종료일
    LocalDate subscribeEndDate;

    // 상장일
    LocalDate listedDate;

    // 종목 코드
    int stockCode;

    @Builder()
    public CrawlerDto(String stockName, int expectedOfferingPriceMin, int expectedOfferingPriceMax,
                      int fixedOfferingPrice, int totalAmount, int competitionRate, int lockupRate, String agents,
                      LocalDate subscribeStartDate, LocalDate subscribeEndDate, LocalDate listedDate, int stockCode) {
        this.stockName = stockName;
        this.expectedOfferingPriceMin = expectedOfferingPriceMin;
        this.expectedOfferingPriceMax = expectedOfferingPriceMax;
        this.fixedOfferingPrice = fixedOfferingPrice;
        this.totalAmount = totalAmount;
        this.competitionRate = competitionRate;
        this.lockupRate = lockupRate;
        this.agents = agents;
        this.subscribeStartDate = subscribeStartDate;
        this.subscribeEndDate = subscribeEndDate;
        this.listedDate = listedDate;
        this.stockCode = stockCode;
    }

    @Override
    public String toString() {
        return "CrawlerDto{" +
                "stockName='" + stockName + '\'' +
                ", expectedOfferingPriceMin=" + expectedOfferingPriceMin +
                ", expectedOfferingPriceMax=" + expectedOfferingPriceMax +
                ", fixedOfferingPrice=" + fixedOfferingPrice +
                ", totalAmount=" + totalAmount +
                ", competitionRate=" + competitionRate +
                ", lockupRate=" + lockupRate +
                ", agents='" + agents + '\'' +
                ", subscribeStartDate=" + subscribeStartDate +
                ", subscribeEndDate=" + subscribeEndDate +
                ", listedDate=" + listedDate +
                ", stockCode='" + stockCode + '\'' +
                '}';
    }
}
