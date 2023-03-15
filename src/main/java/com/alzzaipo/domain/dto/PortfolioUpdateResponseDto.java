package com.alzzaipo.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PortfolioUpdateResponseDto {

    private Long portfolioId;
    private int stockCode;
    private int sharesCnt;
    private int profit;
    private String agents;
    private String memo;

    @Builder
    public PortfolioUpdateResponseDto(Long portfolioId, int stockCode, int sharesCnt, int profit, String agents, String memo) {
        this.portfolioId = portfolioId;
        this.stockCode = stockCode;
        this.sharesCnt = sharesCnt;
        this.profit = profit;
        this.agents = agents;
        this.memo = memo;
    }
}
