package com.alzzaipo.ipo.application.port.out.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateListedIpoCommand {

    private final int stockCode;
    private final int initialMarketPrice;
    private final int profitRate;
}
