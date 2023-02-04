package csct3434.ipo.web.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IPORepository extends JpaRepository<IPO, Long> {
    @Query("select i from IPO i where i.stockCode = ?1")
    IPO findByStockCode(Integer stockCode);
}
