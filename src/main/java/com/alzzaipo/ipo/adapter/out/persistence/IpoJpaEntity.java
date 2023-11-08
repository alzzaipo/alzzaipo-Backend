package com.alzzaipo.ipo.adapter.out.persistence;

import com.alzzaipo.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class IpoJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    // 종목명
    private String stockName;

    // 종목 코드
    @Column(unique = true, nullable = false)
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

    // 상장 여부
    private boolean listed;

    public IpoJpaEntity(String stockName, int stockCode, int expectedOfferingPriceMin, int expectedOfferingPriceMax, int fixedOfferingPrice, int totalAmount, int competitionRate, int lockupRate, String agents, LocalDate subscribeStartDate, LocalDate subscribeEndDate, LocalDate listedDate, int initialMarketPrice, int profitRate) {
        this.stockName = stockName;
        this.stockCode = stockCode;
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
        this.profitRate = profitRate;
    }

    public void changeProfitRate(int profitRate) {
        this.profitRate = profitRate;
    }

    public void changeInitialMarketPrice(int initialMarketPrice) {
        this.initialMarketPrice = initialMarketPrice;
    }

    public void setListed() {
        this.listed = true;
    }
}
