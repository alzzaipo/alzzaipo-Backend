package com.alzzaipo.domain.portfolio;

import com.alzzaipo.domain.ipo.Ipo;
import com.alzzaipo.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class PortfolioTest {

    @Test
    public void 포트폴리오_수익률계산() throws Exception
    {
        //given
        Ipo ipo = Ipo.builder()
                .fixedOfferingPrice(10000)
                .build();

        Portfolio portfolio = Portfolio.builder()
                .member(new Member())
                .ipo(ipo)
                .sharesCnt(10)
                .profit(47000)
                .agents("test")
                .memo("test")
                .build();

        //when
        int oldProfitRate = portfolio.getProfitRate();
        int newProfit = portfolio.changeProfit(40000);
        int newProfitRate = portfolio.getProfitRate();

        //then
        assertThat(oldProfitRate).isEqualTo(47);
        assertThat(newProfit).isEqualTo(40000);
        assertThat(newProfitRate).isEqualTo(40);
    }
}