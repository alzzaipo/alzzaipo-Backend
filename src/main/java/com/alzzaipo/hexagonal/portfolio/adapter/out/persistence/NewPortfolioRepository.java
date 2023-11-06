package com.alzzaipo.hexagonal.portfolio.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NewPortfolioRepository extends JpaRepository<PortfolioJpaEntity, Long> {

    @Query("SELECT p FROM PortfolioJpaEntity p WHERE p.memberJpaEntity.uid = :memberUID")
    List<PortfolioJpaEntity> findByMemberUID(@Param("memberUID") Long memberUID);

    @Query("SELECT p FROM PortfolioJpaEntity p WHERE p.portfolioUID = :portfolioUID")
    Optional<PortfolioJpaEntity> findByPortfolioUID(@Param("portfolioUID") Long portfolioUID);
}
