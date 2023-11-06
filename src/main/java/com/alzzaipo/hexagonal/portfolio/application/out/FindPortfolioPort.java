package com.alzzaipo.hexagonal.portfolio.application.out;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.portfolio.domain.Portfolio;

import java.util.Optional;

public interface FindPortfolioPort {

    Optional<Portfolio> findPortfolio(Uid portfolioUID);
}
