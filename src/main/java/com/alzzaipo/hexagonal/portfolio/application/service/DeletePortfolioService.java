package com.alzzaipo.hexagonal.portfolio.application.service;

import com.alzzaipo.hexagonal.portfolio.application.dto.DeletePortfolioCommand;
import com.alzzaipo.hexagonal.portfolio.application.in.DeletePortfolioUseCase;
import com.alzzaipo.hexagonal.portfolio.application.out.DeletePortfolioPort;
import com.alzzaipo.hexagonal.portfolio.application.out.FindPortfolioPort;
import com.alzzaipo.hexagonal.portfolio.domain.Portfolio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeletePortfolioService implements DeletePortfolioUseCase {

    private final FindPortfolioPort findPortfolioPort;
    private final DeletePortfolioPort deletePortfolioPort;

    @Override
    public void deletePortfolio(DeletePortfolioCommand command) {
        Portfolio targetPortfolio = findPortfolioPort.findPortfolio(command.getPortfolioUID())
                .orElseThrow(() -> new RuntimeException("포트폴리오 조회 실패"));

        if (!targetPortfolio.getMemberUID().equals(command.getMemberUID())) {
            throw new RuntimeException("포트폴리오 권한 없음");
        }

        deletePortfolioPort.deletePortfolio(command.getPortfolioUID());
    }
}
