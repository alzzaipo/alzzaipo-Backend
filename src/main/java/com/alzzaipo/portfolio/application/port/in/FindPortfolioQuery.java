package com.alzzaipo.portfolio.application.port.in;

import com.alzzaipo.portfolio.application.dto.FindPortfolioCommand;
import com.alzzaipo.portfolio.application.dto.PortfolioView;

public interface FindPortfolioQuery {

    PortfolioView findPortfolio(FindPortfolioCommand command);
}
