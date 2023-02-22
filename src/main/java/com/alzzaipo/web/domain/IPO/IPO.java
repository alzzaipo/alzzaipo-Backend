package com.alzzaipo.web.domain.IPO;

import com.alzzaipo.web.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class IPO extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    // 종목명
    private String stockName;

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

    // 종목 코드
    @Column(unique = true, nullable = false)
    private int stockCode;

    @Builder
    public IPO(String stockName, int expectedOfferingPriceMin, int expectedOfferingPriceMax, int fixedOfferingPrice,
               int totalAmount, int competitionRate, int lockupRate, String agents,
               LocalDate subscribeStartDate, LocalDate subscribeEndDate, LocalDate listedDate, int initialMarketPrice, int stockCode) {
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
        this.initialMarketPrice = initialMarketPrice;
        this.stockCode = stockCode;

        if(initialMarketPrice > 0) {
            this.profitRate = (int)((double)(initialMarketPrice - fixedOfferingPrice) / (double)fixedOfferingPrice * 100);
        } else {
            this.profitRate = 0;
        }
    }
}