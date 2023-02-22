package com.alzzaipo.web.dto;

import lombok.Getter;

@Getter
public class IPOListResponseDto {
    private String stockName;
    private int stockCode;

    public IPOListResponseDto(String stockName, int stockCode) {
        this.stockName = stockName;
        this.stockCode = stockCode;
    }
}
