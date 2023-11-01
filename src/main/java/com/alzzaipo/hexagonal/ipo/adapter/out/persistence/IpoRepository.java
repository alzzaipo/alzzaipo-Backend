package com.alzzaipo.hexagonal.ipo.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IpoRepository extends JpaRepository<IpoJpaEntity, Long> {

    @Query("SELECT i FROM IpoJpaEntity i WHERE i.stockCode = ?1")
    Optional<IpoJpaEntity> findByStockCode(int stockCode);
}
