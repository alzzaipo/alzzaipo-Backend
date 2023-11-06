package com.alzzaipo.hexagonal.portfolio.application.in;

import com.alzzaipo.hexagonal.portfolio.application.dto.RegisterPortfolioCommand;

public interface RegisterPortfolioUseCase {

    void registerPortfolio(RegisterPortfolioCommand command);
}
