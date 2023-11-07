package com.alzzaipo.portfolio.application.service;

import com.alzzaipo.ipo.application.port.out.FindIpoByStockCodePort;
import com.alzzaipo.ipo.domain.Ipo;
import com.alzzaipo.portfolio.application.dto.UpdatePortfolioCommand;
import com.alzzaipo.portfolio.application.in.UpdatePortfolioUseCase;
import com.alzzaipo.portfolio.application.out.FindPortfolioPort;
import com.alzzaipo.portfolio.application.out.UpdatePortfolioPort;
import com.alzzaipo.portfolio.domain.Portfolio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdatePortfolioService implements UpdatePortfolioUseCase {

    private final FindPortfolioPort findPortfolioPort;
    private final FindIpoByStockCodePort findIpoByStockCodePort;
    private final UpdatePortfolioPort updatePortfolioPort;

    @Override
    public void updatePortfolio(UpdatePortfolioCommand command) {
        Portfolio targetPortfolio = findPortfolioPort.findPortfolio(command.getPortfolioUID())
                .orElseThrow(() -> new RuntimeException("포트폴리오 조회 실패"));

        if (!targetPortfolio.getMemberUID().equals(command.getMemberUID())) {
            throw new RuntimeException("포트폴리오 권한 없음");
        }

        Ipo ipo = findIpoByStockCodePort.findIpoByStockCodePort(command.getStockCode())
                .orElseThrow(() -> new RuntimeException("공모주 조회 실패"));

        Portfolio newPortfolio = createPortfolio(command, ipo);

        updatePortfolioPort.updatePortfolio(newPortfolio);
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
