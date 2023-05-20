package com.alzzaipo.domain.ipo;

import com.alzzaipo.dto.ipo.IpoListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IpoRepository extends JpaRepository<Ipo, Long> {
    @Query("SELECT i FROM Ipo i WHERE i.stockCode = ?1")
    Optional<Ipo> findByStockCode(int stockCode);

    @Query("SELECT i FROM Ipo i WHERE i.listedDate >= ?1 AND i.listedDate <= ?2 AND i.competitionRate >= ?3 AND i.lockupRate >= ?4")
    List<Ipo> analyze(LocalDate from, LocalDate to, int minCompetitionRate, int minLockupRate);

    @Query("SELECT new com.alzzaipo.dto.ipo.IpoListDto(i.stockName, i.stockCode) FROM Ipo i")
    List<IpoListDto> findAllIpoListDto();
}
