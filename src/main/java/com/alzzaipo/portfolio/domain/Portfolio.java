package com.alzzaipo.portfolio.domain;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.util.UidGenerator;
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

    public Portfolio(Uid portfolioUID, Uid memberUID, String stockName, int stockCode, int sharesCnt, Long profit, Long profitRate, String agents, String memo) {
        this.portfolioUID = portfolioUID;
        this.memberUID = memberUID;
        this.stockName = stockName;
        this.stockCode = stockCode;
        this.sharesCnt = sharesCnt;
        this.profit = profit;
        this.profitRate = profitRate;
        this.agents = agents;
        this.memo = memo;
    }

    public static Portfolio create(Uid memberUID, String stockName, int stockCode, int sharesCnt, Long profit, Long profitRate, String agents, String memo) {
        return new Portfolio(
                UidGenerator.generate(),
                memberUID,
                stockName,
                stockCode,
                sharesCnt,
                profit,
                profitRate,
                agents,
                memo);
    }
}
