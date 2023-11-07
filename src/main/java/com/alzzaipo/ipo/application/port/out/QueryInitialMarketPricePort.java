package com.alzzaipo.ipo.application.port.out;

import com.alzzaipo.ipo.application.port.out.dto.QueryInitialMarketPriceResult;

import java.time.LocalDate;

public interface QueryInitialMarketPricePort {

    QueryInitialMarketPriceResult queryInitialMarketPrice(int stockCode, LocalDate date);
}
