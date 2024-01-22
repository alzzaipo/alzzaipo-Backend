package com.alzzaipo.ipo.application.port.out;

import java.time.LocalDate;

public interface QueryInitialMarketPricePort {

    // 조회 불가 시, -1 반환
    int query(int stockCode, LocalDate date);
}
