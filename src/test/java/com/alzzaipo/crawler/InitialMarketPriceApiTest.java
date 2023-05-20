package com.alzzaipo.crawler;

import com.alzzaipo.util.InitialMarketPriceApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class InitialMarketPriceApiTest {

    private final InitialMarketPriceApi initialMarketPriceApi;

    @Autowired
    public InitialMarketPriceApiTest(InitialMarketPriceApi initialMarketPriceApi) {
        this.initialMarketPriceApi = initialMarketPriceApi;
    }

    @Test
    void 상장일_시초가_조회() {
        int 피코그램_시초가 = initialMarketPriceApi.getInitialMarketPrice(376180, LocalDate.parse("2021-11-03"));
        assertThat(피코그램_시초가).isEqualTo(25000);

        int 엔젯_시초가 = initialMarketPriceApi.getInitialMarketPrice(419080, LocalDate.parse("2022-11-18"));
        assertThat(엔젯_시초가).isEqualTo(9000);

        int 카카오페이_시초가 = initialMarketPriceApi.getInitialMarketPrice(377300, LocalDate.parse("2021-11-03"));
        assertThat(카카오페이_시초가).isEqualTo(180000);

        int 미래반도체_시초가 = initialMarketPriceApi.getInitialMarketPrice(254490, LocalDate.parse("2023-01-27"));
        assertThat(미래반도체_시초가).isEqualTo(12000);

        int 신영스팩_시초가 = initialMarketPriceApi.getInitialMarketPrice(445970, LocalDate.parse("2022-12-27"));
        assertThat(신영스팩_시초가).isEqualTo(1985);
    }
}