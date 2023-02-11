package csct3434.ipo.web.domain.IPO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface IPORepository extends JpaRepository<IPO, Long> {
    @Query("select i from IPO i where i.stockCode = ?1")
    IPO findByStockCode(int stockCode);

    @Query("select i from IPO i where i.listedDate >= ?1 and i.listedDate <= ?2 and i.competitionRate >= ?3 and i.lockupRate >= ?4")
    List<IPO> analyze(LocalDate from, LocalDate to, int competitionRate, int lockupRate);
}
