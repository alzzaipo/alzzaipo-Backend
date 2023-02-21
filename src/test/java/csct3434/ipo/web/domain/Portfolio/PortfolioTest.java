package csct3434.ipo.web.domain.Portfolio;

import csct3434.ipo.service.MemberService;
import csct3434.ipo.web.domain.IPO.IPO;
import csct3434.ipo.web.domain.IPO.IPORepository;
import csct3434.ipo.web.domain.Member.Member;
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
                .profit(200000)
                .agents("KB증권")
                .memo("테스트")
                .build();

        ipoRepository.save(ipo);
        memberService.save(member);
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        //when
        int initialProfitRate = savedPortfolio.getProfitRate();
        int changedProfit = savedPortfolio.changeProfit(150000);
        int changedProfitRate = savedPortfolio.getProfitRate();

        //then
        assertThat(initialProfitRate).isEqualTo(100);
        assertThat(changedProfit).isEqualTo(150000);
        assertThat(changedProfitRate).isEqualTo(50);
    }
}