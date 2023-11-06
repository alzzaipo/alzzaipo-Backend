package com.alzzaipo.hexagonal.portfolio.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class PortfolioPersistenceAdapter {

    private final NewPortfolioRepository portfolioRepository;
}
