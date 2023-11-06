package com.alzzaipo.hexagonal.portfolio.application.in;

import com.alzzaipo.hexagonal.portfolio.application.dto.DeletePortfolioCommand;

public interface DeletePortfolioUseCase {

    void deletePortfolio(DeletePortfolioCommand command);
}
