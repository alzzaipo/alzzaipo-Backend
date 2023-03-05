package com.alzzaipo.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class PortfolioCreateRequestDto {

    private int stockCode;
    private int sharesCnt;
    private int profit;
    private String agents;
    private String memo;

    @Builder
    public PortfolioCreateRequestDto(int stockCode, int sharesCnt, int profit, String agents, String memo) {
        this.stockCode = stockCode;
        this.sharesCnt = sharesCnt;
        this.profit = profit;
        this.agents = agents;
        this.memo = memo;
    }
}
