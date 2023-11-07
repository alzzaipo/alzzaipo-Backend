package com.alzzaipo.hexagonal.ipo.application.port.out.dto;

import lombok.Getter;

@Getter
public class UpdateListedIpoCommand {

    private final int stockCode;
    private final int initialMarketPrice;
    private final int profitRate;

    public UpdateListedIpoCommand(int stockCode, int initialMarketPrice, int profitRate) {
        this.stockCode = stockCode;
        this.initialMarketPrice = initialMarketPrice;
        this.profitRate = profitRate;
    }
}
