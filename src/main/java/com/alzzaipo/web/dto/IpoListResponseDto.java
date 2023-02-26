package com.alzzaipo.web.dto;

import lombok.Getter;

@Getter
public class IpoListResponseDto {
    private String stockName;
    private int stockCode;

    public IpoListResponseDto(String stockName, int stockCode) {
        this.stockName = stockName;
        this.stockCode = stockCode;
    }
}
