package com.alzzaipo.hexagonal.portfolio.application.out;

import com.alzzaipo.hexagonal.common.Uid;

public interface DeletePortfolioPort {

    void deletePortfolio(Uid portfolioUID);
}
