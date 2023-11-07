package com.alzzaipo.hexagonal.ipo.application.port.out;

import com.alzzaipo.hexagonal.ipo.application.port.out.dto.QueryInitialMarketPriceResult;

import java.time.LocalDate;

public interface QueryInitialMarketPricePort {

    QueryInitialMarketPriceResult queryInitialMarketPrice(int stockCode, LocalDate date);
}
