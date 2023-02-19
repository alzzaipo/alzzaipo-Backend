package csct3434.ipo.web.domain.Portfolio;

import csct3434.ipo.web.dto.PortfolioSaveRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    @Query("SELECT new csct3434.ipo.web.dto.PortfolioSaveRequestDto(p.id, p.member.id, p.ipo.stockCode, p.sharesCnt, p.profit, p.agents, p.memo)" +
            "FROM Portfolio p WHERE p.id = :portfolioId")
    public Optional<PortfolioSaveRequestDto> getPortfolioSaveRequestDto(Long portfolioId);
}
