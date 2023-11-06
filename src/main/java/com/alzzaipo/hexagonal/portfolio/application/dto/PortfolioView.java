package com.alzzaipo.hexagonal.portfolio.application.dto;

import lombok.Getter;

@Getter
public class PortfolioView {

    private final Long uid;

    private final String stockName;

    private final int stockCode;

    private final int sharesCnt;

    private final Long profit;

    private final Long profitRate;

    private final String agents;

    private final String memo;

    public PortfolioView(Long uid, String stockName, int stockCode, int sharesCnt, Long profit, Long profitRate, String agents, String memo) {
        this.uid = uid;
        this.stockName = stockName;
        this.stockCode = stockCode;
        this.sharesCnt = sharesCnt;
        this.profit = profit;
        this.profitRate = profitRate;
        this.agents = agents;
        this.memo = memo;
    }
}
