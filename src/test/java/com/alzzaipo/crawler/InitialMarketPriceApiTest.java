package com.alzzaipo.crawler;

import com.alzzaipo.ipo.application.port.out.QueryInitialMarketPricePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class InitialMarketPriceApiTest {

    @Autowired
    private QueryInitialMarketPricePort queryInitialMarketPricePort;

    @Test
    void 상장일_시초가_조회() {
        int 피코그램_시초가 = queryInitialMarketPricePort.queryInitialMarketPrice(376180, LocalDate.parse("2021-11-03")).getInitialMarketPrice();
        assertThat(피코그램_시초가).isEqualTo(25000);

        int 엔젯_시초가 = queryInitialMarketPricePort.queryInitialMarketPrice(419080, LocalDate.parse("2022-11-18")).getInitialMarketPrice();
        assertThat(엔젯_시초가).isEqualTo(9000);

        int 카카오페이_시초가 = queryInitialMarketPricePort.queryInitialMarketPrice(377300, LocalDate.parse("2021-11-03")).getInitialMarketPrice();
        assertThat(카카오페이_시초가).isEqualTo(180000);

        int 미래반도체_시초가 = queryInitialMarketPricePort.queryInitialMarketPrice(254490, LocalDate.parse("2023-01-27")).getInitialMarketPrice();
        assertThat(미래반도체_시초가).isEqualTo(12000);

        int 신영스팩_시초가 = queryInitialMarketPricePort.queryInitialMarketPrice(445970, LocalDate.parse("2022-12-27")).getInitialMarketPrice();
        assertThat(신영스팩_시초가).isEqualTo(1985);
    }
}