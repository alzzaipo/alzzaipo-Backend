package com.alzzaipo.hexagonal.portfolio.application.in;

import com.alzzaipo.hexagonal.portfolio.application.dto.FindPortfolioCommand;
import com.alzzaipo.hexagonal.portfolio.application.dto.PortfolioView;

public interface FindPortfolioQuery {

    PortfolioView findPortfolio(FindPortfolioCommand command);
}
