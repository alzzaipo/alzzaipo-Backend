package com.alzzaipo.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class PortfolioSaveRequestDto {

    private Long portfolioId;
    private int stockCode;
    private int sharesCnt;
    private int profit;
    private String agents;
    private String memo;

    @Builder
    public PortfolioSaveRequestDto(int stockCode, int sharesCnt, int profit, String agents, String memo) {
        this.stockCode = stockCode;
        this.sharesCnt = sharesCnt;
        this.profit = profit;
        this.agents = agents;
        this.memo = memo;
    }

    @Builder
    public PortfolioSaveRequestDto(Long portfolioId, int stockCode, int sharesCnt, int profit, String agents, String memo) {
        this.portfolioId = portfolioId;
        this.stockCode = stockCode;
        this.sharesCnt = sharesCnt;
        this.profit = profit;
        this.agents = agents;
        this.memo = memo;
    }
}
