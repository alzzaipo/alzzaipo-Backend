package com.alzzaipo.ipo.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface IpoRepository extends JpaRepository<IpoJpaEntity, Long> {

    Optional<IpoJpaEntity> findByStockCode(int stockCode);

    @Query("SELECT i FROM IpoJpaEntity i " +
            "WHERE i.initialMarketPrice > 0 " +
            "   AND YEAR(i.listedDate) >= :from " +
            "   AND YEAR(i.listedDate) <= :to " +
            "   AND i.competitionRate >= :minCompetitionRate " +
            "   AND i.lockupRate >= :minLockupRate")
    List<IpoJpaEntity> findAnalyzeIpoProfitRateTarget(
            @Param("from") int from,
            @Param("to") int to,
            @Param("minCompetitionRate") int minCompetitionRate,
            @Param("minLockupRate") int minLockupRate);

    @Query("SELECT i FROM IpoJpaEntity i WHERE i.listed = false")
    List<IpoJpaEntity> findNotListedIpos();

    boolean existsByStockCode(int stockCode);
}
