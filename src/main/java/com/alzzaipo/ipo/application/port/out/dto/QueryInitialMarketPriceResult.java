package com.alzzaipo.ipo.application.port.out.dto;

import lombok.Getter;

@Getter
public class QueryInitialMarketPriceResult {

    private final boolean success;
    private final int initialMarketPrice;

    public QueryInitialMarketPriceResult(boolean success, int initialMarketPrice) {
        this.success = success;
        this.initialMarketPrice = initialMarketPrice;
    }
}
