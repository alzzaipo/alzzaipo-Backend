package com.alzzaipo.domain.portfolio;

import com.alzzaipo.domain.dto.PortfolioUpdateDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    @Query(value = "SELECT new com.alzzaipo.domain.dto.PortfolioUpdateDto(p.id, p.ipo.stockCode, p.sharesCnt, p.profit, p.agents, p.memo)" +
            "FROM Portfolio p WHERE p.id = :portfolioId")
    public Optional<PortfolioUpdateDto> getPortfolioUpdateDto(Long portfolioId);
}
