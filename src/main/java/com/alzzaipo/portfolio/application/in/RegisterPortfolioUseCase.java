package com.alzzaipo.portfolio.application.in;

import com.alzzaipo.portfolio.application.dto.RegisterPortfolioCommand;

public interface RegisterPortfolioUseCase {

    void registerPortfolio(RegisterPortfolioCommand command);
}
