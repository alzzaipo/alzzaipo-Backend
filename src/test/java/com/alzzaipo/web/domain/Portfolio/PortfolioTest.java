package com.alzzaipo.web.domain.Portfolio;

import com.alzzaipo.domain.ipo.IPO;
import com.alzzaipo.domain.portfolio.Portfolio;
import com.alzzaipo.domain.portfolio.PortfolioRepository;
import com.alzzaipo.service.UserService;
import com.alzzaipo.domain.ipo.IPORepository;
import com.alzzaipo.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class PortfolioTest {

    private final IPORepository ipoRepository;
    private final PortfolioRepository portfolioRepository;
    private final UserService userService;

    @Autowired
    public PortfolioTest(IPORepository ipoRepository, PortfolioRepository portfolioRepository, UserService userService) {
        this.ipoRepository = ipoRepository;
        this.portfolioRepository = portfolioRepository;
        this.userService = userService;
    }

    @Test
    public void 포트폴리오_수익률계산() throws Exception
    {
        //given
        IPO ipo = IPO.builder()
                .fixedOfferingPrice(10000)
                .build();

        User user = User.builder().build();

        Portfolio portfolio = Portfolio.builder()
                .user(user)
                .ipo(ipo)
                .sharesCnt(10)
                .profit(47000)
                .agents("KB증권")
                .memo("테스트")
                .build();

        ipoRepository.save(ipo);
        userService.save(user);
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        //when
        int initialProfitRate = savedPortfolio.getProfitRate();
        int changedProfit = savedPortfolio.changeProfit(40000);
        int changedProfitRate = savedPortfolio.getProfitRate();

        //then
        assertThat(initialProfitRate).isEqualTo(47);
        assertThat(changedProfit).isEqualTo(40000);
        assertThat(changedProfitRate).isEqualTo(40);
    }
}