package com.alzzaipo.portfolio.application.service;

import com.alzzaipo.ipo.application.port.out.FindIpoByStockCodePort;
import com.alzzaipo.ipo.domain.Ipo;
import com.alzzaipo.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.portfolio.application.dto.RegisterPortfolioCommand;
import com.alzzaipo.portfolio.application.in.RegisterPortfolioUseCase;
import com.alzzaipo.portfolio.application.out.RegisterPortfolioPort;
import com.alzzaipo.portfolio.domain.Portfolio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterPortfolioService implements RegisterPortfolioUseCase {

    private final FindMemberPort findMemberPort;
    private final FindIpoByStockCodePort findIpoByStockCodePort;
    private final RegisterPortfolioPort registerPortfolioPort;

    @Override
    public void registerPortfolio(RegisterPortfolioCommand command) {
        findMemberPort.findMember(command.getMemberUID())
                .orElseThrow(() -> new RuntimeException("멤버 조회 실패"));

        Ipo ipo = findIpoByStockCodePort.findIpoByStockCodePort(command.getStockCode())
                .orElseThrow(() -> new RuntimeException("공모주 조회 실패"));

        Portfolio portfolio = createPortfolio(command, ipo);

        registerPortfolioPort.registerPortfolio(portfolio);
    }

    private Portfolio createPortfolio(RegisterPortfolioCommand command, Ipo ipo) {
        return Portfolio.create(
                command.getMemberUID(),
                ipo.getStockName(),
                command.getStockCode(),
                command.getSharesCnt(),
                command.getProfit(),
                calculateProfitRate(command, ipo),
                command.getAgents(),
                command.getMemo());
    }

    private Long calculateProfitRate(RegisterPortfolioCommand command, Ipo ipo) {
        Long profit = command.getProfit();
        int sharesCnt = command.getSharesCnt();
        int fixedOfferingPrice = ipo.getFixedOfferingPrice();

        if (sharesCnt > 0 && fixedOfferingPrice > 0) {
            Long profitPerShare = profit / sharesCnt;
            return (profitPerShare * 100 / fixedOfferingPrice);
        }
        return 0L;
    }
}
