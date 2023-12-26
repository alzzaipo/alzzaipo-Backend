package com.alzzaipo.portfolio.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends JpaRepository<PortfolioJpaEntity, Long> {

    List<PortfolioJpaEntity> findByMemberJpaEntityId(long memberId);
}
