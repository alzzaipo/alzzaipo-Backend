package com.alzzaipo.portfolio.application.in;

import com.alzzaipo.portfolio.application.dto.DeletePortfolioCommand;

public interface DeletePortfolioUseCase {

    void deletePortfolio(DeletePortfolioCommand command);
}
