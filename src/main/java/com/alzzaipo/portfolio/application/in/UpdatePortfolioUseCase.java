package com.alzzaipo.portfolio.application.in;

import com.alzzaipo.portfolio.application.dto.UpdatePortfolioCommand;

public interface UpdatePortfolioUseCase {

    void updatePortfolio(UpdatePortfolioCommand command);
}
