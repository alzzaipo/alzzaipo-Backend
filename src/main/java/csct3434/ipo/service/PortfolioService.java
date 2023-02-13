package csct3434.ipo.service;

import csct3434.ipo.web.domain.Portfolio.Portfolio;
import csct3434.ipo.web.domain.Portfolio.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public Portfolio save(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

}
