package com.alzzaipo.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class PortfolioListDto {

    private Long portfolioId;

    private String stockName;

    private int stockCode;

    private LocalDate subscribeStartDate;

    private LocalDate subscribeEndDate;

    private LocalDate listedDate;

    private int fixedOfferingPrice;

    private String agents;

    private int sharesCnt;

    private int profit;

    private int profitRate;

    private String memo;

    @Builder
    public PortfolioListDto(Long portfolioId, String stockName, int stockCode, LocalDate subscribeStartDate, LocalDate subscribeEndDate,
                            LocalDate listedDate, int fixedOfferingPrice, String agents,
                            int sharesCnt, int profit, int profitRate, String memo) {
        this.portfolioId = portfolioId;
        this.stockName = stockName;
        this.stockCode = stockCode;
        this.subscribeStartDate = subscribeStartDate;
        this.subscribeEndDate = subscribeEndDate;
        this.listedDate = listedDate;
        this.fixedOfferingPrice = fixedOfferingPrice;
        this.agents = agents;
        this.sharesCnt = sharesCnt;
        this.profit = profit;
        this.profitRate = profitRate;
        this.memo = memo;
    }
}
