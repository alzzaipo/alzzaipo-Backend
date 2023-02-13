package csct3434.ipo.web.domain.Portfolio;

import csct3434.ipo.web.domain.IPO.IPO;
import csct3434.ipo.web.domain.IPO.IPORepository;
import csct3434.ipo.web.domain.Member.Member;
import csct3434.ipo.web.domain.Member.MemberRepository;
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
    private final MemberRepository memberRepository;

    @Autowired
    public PortfolioRepositoryTest(IPORepository ipoRepository, PortfolioRepository portfolioRepository, MemberRepository memberRepository) {
        this.ipoRepository = ipoRepository;
        this.portfolioRepository = portfolioRepository;
        this.memberRepository = memberRepository;
    }

    @Test
    public void 저장조회삭제() throws Exception {
        //given
        IPO ipo = IPO.builder()
                .fixedOfferingPrice(10000)
                .build();

        Member member = Member.builder().build();

        Portfolio portfolio = Portfolio.createPortfolio(member, ipo, 10, 200000);

        ipoRepository.save(ipo);
        memberRepository.save(member);

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