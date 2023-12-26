package com.alzzaipo.portfolio.application.port.out;

import com.alzzaipo.common.Id;
import com.alzzaipo.portfolio.domain.Portfolio;

import java.util.Optional;

public interface FindPortfolioPort {

    Optional<Portfolio> findPortfolio(Id portfolioId);
}
