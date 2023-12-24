package com.alzzaipo.portfolio.application.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MemberPortfolioSummary {

    private final long totalProfit;
    private final long averageProfitRate;
    private final List<PortfolioView> portfolioList;

    private MemberPortfolioSummary(Long totalProfit, Long averageProfitRate, List<PortfolioView> portfolioList) {
        this.totalProfit = totalProfit;
        this.averageProfitRate = averageProfitRate;
        this.portfolioList = portfolioList;
    }

    public static MemberPortfolioSummary build(List<PortfolioView> portfolioList) {
        long totalProfit = portfolioList.stream()
            .mapToLong(PortfolioView::getProfit)
            .sum();

        long averageProfitRate = (long) portfolioList.stream()
            .mapToDouble(PortfolioView::getProfitRate)
            .average()
            .orElse(0.0);

        return new MemberPortfolioSummary(totalProfit, averageProfitRate, portfolioList);
    }
}
