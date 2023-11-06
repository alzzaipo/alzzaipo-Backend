package com.alzzaipo.hexagonal.portfolio.application.service;

import com.alzzaipo.hexagonal.ipo.application.port.out.FindIpoByStockCodePort;
import com.alzzaipo.hexagonal.ipo.domain.Ipo;
import com.alzzaipo.hexagonal.portfolio.application.dto.UpdatePortfolioCommand;
import com.alzzaipo.hexagonal.portfolio.application.in.UpdatePortfolioUseCase;
import com.alzzaipo.hexagonal.portfolio.application.out.UpdatePortfolioPort;
import com.alzzaipo.hexagonal.portfolio.domain.Portfolio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdatePortfolioService implements UpdatePortfolioUseCase {

    private final FindIpoByStockCodePort findIpoByStockCodePort;
    private final UpdatePortfolioPort updatePortfolioPort;

    @Override
    public void updatePortfolio(UpdatePortfolioCommand command) {
        Ipo ipo = findIpoByStockCodePort.findIpoByStockCodePort(command.getStockCode())
                .orElseThrow(() -> new RuntimeException("공모주 조회 실패"));

        Portfolio portfolio = createPortfolio(command, ipo);

        updatePortfolioPort.updatePortfolio(portfolio);
    }

    private Portfolio createPortfolio(UpdatePortfolioCommand command, Ipo ipo) {
        return new Portfolio(
                command.getPortfolioUID(),
                command.getMemberUID(),
                ipo.getStockName(),
                ipo.getStockCode(),
                command.getSharesCnt(),
                command.getProfit(),
                calculateProfitRate(command, ipo),
                command.getAgents(),
                command.getMemo());
    }

    private Long calculateProfitRate(UpdatePortfolioCommand command, Ipo ipo) {
        Long profit = command.getProfit();
        int sharesCnt = command.getSharesCnt();
        int fixedOfferingPrice = ipo.getFixedOfferingPrice();

        if (sharesCnt > 0 && fixedOfferingPrice > 0) {
            long profitPerShare = profit / sharesCnt;
            return (profitPerShare * 100 / fixedOfferingPrice);
        }
        return 0L;
    }
}
