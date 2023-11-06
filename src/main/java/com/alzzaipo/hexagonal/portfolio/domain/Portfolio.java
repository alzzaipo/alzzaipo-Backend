package com.alzzaipo.hexagonal.portfolio.domain;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.common.util.UidGenerator;
import lombok.Getter;

@Getter
public class Portfolio {

    private final Uid portfolioUID;

    private final Uid memberUID;

    private String stockName;

    private int stockCode;

    private int sharesCnt;

    private Long profit;

    private Long profitRate;

    private String agents;

    private String memo;

    public Portfolio(Uid memberUID, String stockName, int stockCode, int sharesCnt, Long profit, Long profitRate, String agents, String memo) {
        this.portfolioUID = UidGenerator.generate();
        this.stockName = stockName;
        this.memberUID = memberUID;
        this.stockCode = stockCode;
        this.sharesCnt = sharesCnt;
        this.profit = profit;
        this.profitRate = profitRate;
        this.agents = agents;
        this.memo = memo;
    }
}
