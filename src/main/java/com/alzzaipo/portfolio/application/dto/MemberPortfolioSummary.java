package com.alzzaipo.portfolio.application.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MemberPortfolioSummary {

    private final Long totalProfit;
    private final Long averageProfitRate;
    private final List<PortfolioView> portfolioList;

    public MemberPortfolioSummary(List<PortfolioView> portfolioList) {
        this.portfolioList = portfolioList;

        this.averageProfitRate = (long) portfolioList.stream()
                .mapToDouble(PortfolioView::getProfitRate)
                .average()
                .orElse(0.0);

        this.totalProfit = portfolioList.stream()
                .mapToLong(PortfolioView::getProfit)
                .sum();
    }
}
