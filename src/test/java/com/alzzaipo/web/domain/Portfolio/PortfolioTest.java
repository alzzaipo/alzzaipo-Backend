package com.alzzaipo.web.domain.Portfolio;

import com.alzzaipo.domain.IPO.IPO;
import com.alzzaipo.domain.Portfolio.Portfolio;
import com.alzzaipo.domain.Portfolio.PortfolioRepository;
import com.alzzaipo.service.MemberService;
import com.alzzaipo.domain.IPO.IPORepository;
import com.alzzaipo.domain.Member.Member;
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
    private final MemberService memberService;

    @Autowired
    public PortfolioTest(IPORepository ipoRepository, PortfolioRepository portfolioRepository, MemberService memberService) {
        this.ipoRepository = ipoRepository;
        this.portfolioRepository = portfolioRepository;
        this.memberService = memberService;
    }

    @Test
    public void 포트폴리오_수익률계산() throws Exception
    {
        //given
        IPO ipo = IPO.builder()
                .fixedOfferingPrice(10000)
                .build();

        Member member = Member.builder().build();

        Portfolio portfolio = Portfolio.builder()
                .member(member)
                .ipo(ipo)
                .sharesCnt(10)
                .profit(47000)
                .agents("KB증권")
                .memo("테스트")
                .build();

        ipoRepository.save(ipo);
        memberService.save(member);
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