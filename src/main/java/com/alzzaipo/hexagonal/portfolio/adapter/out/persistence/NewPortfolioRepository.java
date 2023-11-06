package com.alzzaipo.hexagonal.portfolio.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NewPortfolioRepository extends JpaRepository<PortfolioJpaEntity, Long> {
}
