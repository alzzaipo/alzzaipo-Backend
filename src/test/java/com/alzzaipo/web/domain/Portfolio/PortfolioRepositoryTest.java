package com.alzzaipo.web.domain.Portfolio;

import com.alzzaipo.domain.ipo.IPO;
import com.alzzaipo.domain.ipo.IPORepository;
import com.alzzaipo.domain.user.User;
import com.alzzaipo.domain.user.UserRepository;
import com.alzzaipo.domain.portfolio.Portfolio;
import com.alzzaipo.domain.portfolio.PortfolioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class PortfolioRepositoryTest {

    private final IPORepository ipoRepository;
    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    @Autowired
    public PortfolioRepositoryTest(IPORepository ipoRepository, PortfolioRepository portfolioRepository, UserRepository userRepository) {
        this.ipoRepository = ipoRepository;
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
    }

    @Test
    public void 저장조회삭제() throws Exception {
        //given
        IPO ipo = IPO.builder()
                .fixedOfferingPrice(10000)
                .build();

        User user = User.builder().build();

        Portfolio portfolio = Portfolio.builder()
                .user(user)
                .ipo(ipo)
                .sharesCnt(2)
                .profit(15000)
                .agents("KB증권")
                .memo("테스트")
                .build();

        ipoRepository.save(ipo);
        userRepository.save(user);

        //when
        portfolioRepository.save(portfolio);
        Portfolio findPortfolio = portfolioRepository.findById(portfolio.getId()).get();

        //then
        assertThat(portfolio).isSameAs(findPortfolio);


        //when
        portfolioRepository.delete(findPortfolio);

        //then
        assertThat(portfolioRepository.findAll().size()).isEqualTo(0);
    }

}