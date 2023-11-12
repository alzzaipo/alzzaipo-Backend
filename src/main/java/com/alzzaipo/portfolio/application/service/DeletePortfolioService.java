package com.alzzaipo.portfolio.application.service;

import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.portfolio.application.dto.DeletePortfolioCommand;
import com.alzzaipo.portfolio.application.in.DeletePortfolioUseCase;
import com.alzzaipo.portfolio.application.out.DeletePortfolioPort;
import com.alzzaipo.portfolio.application.out.FindPortfolioPort;
import com.alzzaipo.portfolio.domain.Portfolio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeletePortfolioService implements DeletePortfolioUseCase {

    private final FindPortfolioPort findPortfolioPort;
    private final DeletePortfolioPort deletePortfolioPort;

    @Override
    public void deletePortfolio(DeletePortfolioCommand command) {
        Portfolio targetPortfolio = findPortfolioPort.findPortfolio(command.getPortfolioUID())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "포트폴리오 조회 실패"));

        if (!targetPortfolio.getMemberUID().equals(command.getMemberUID())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "포트폴리오 권한 없음");
        }

        deletePortfolioPort.deletePortfolio(command.getPortfolioUID());
    }
}
