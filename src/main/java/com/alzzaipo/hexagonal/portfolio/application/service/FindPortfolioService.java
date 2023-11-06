package com.alzzaipo.hexagonal.portfolio.application.service;

import com.alzzaipo.hexagonal.portfolio.application.dto.FindPortfolioCommand;
import com.alzzaipo.hexagonal.portfolio.application.dto.PortfolioView;
import com.alzzaipo.hexagonal.portfolio.application.in.FindPortfolioQuery;
import com.alzzaipo.hexagonal.portfolio.application.out.FindPortfolioPort;
import com.alzzaipo.hexagonal.portfolio.domain.Portfolio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindPortfolioService implements FindPortfolioQuery {

    private final FindPortfolioPort findPortfolioPort;

    @Override
    public PortfolioView findPortfolio(FindPortfolioCommand command) {
        Portfolio portfolio = findPortfolioPort.findPortfolio(command.getPortfolioUID())
                .orElseThrow(() -> new RuntimeException("포트폴리오 조회 실패"));

        if (!portfolio.getMemberUID().equals(command.getMemberUID())) {
            throw new RuntimeException("포트폴리오 권한 없음");
        }

        return toViewModel(portfolio);
    }

    public PortfolioView toViewModel(Portfolio portfolio) {
        return new PortfolioView(
                portfolio.getPortfolioUID().get(),
                portfolio.getStockName(),
                portfolio.getStockCode(),
                portfolio.getSharesCnt(),
                portfolio.getProfit(),
                portfolio.getProfitRate(),
                portfolio.getAgents(),
                portfolio.getMemo());
    }
}
