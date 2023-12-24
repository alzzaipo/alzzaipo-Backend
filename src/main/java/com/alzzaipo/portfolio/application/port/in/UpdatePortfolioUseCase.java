package com.alzzaipo.portfolio.application.port.in;

import com.alzzaipo.portfolio.application.dto.UpdatePortfolioCommand;

public interface UpdatePortfolioUseCase {

    void updatePortfolio(UpdatePortfolioCommand command);
}
