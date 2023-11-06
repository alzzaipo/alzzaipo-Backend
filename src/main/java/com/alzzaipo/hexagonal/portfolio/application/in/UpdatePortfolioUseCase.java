package com.alzzaipo.hexagonal.portfolio.application.in;

import com.alzzaipo.hexagonal.portfolio.application.dto.UpdatePortfolioCommand;

public interface UpdatePortfolioUseCase {

    void updatePortfolio(UpdatePortfolioCommand updatePortfolioCommand);
}
