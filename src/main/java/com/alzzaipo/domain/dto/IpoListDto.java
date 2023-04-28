package com.alzzaipo.domain.dto;

import lombok.Getter;

@Getter
public class IpoListDto {
    private String stockName;
    private int stockCode;

    public IpoListDto(String stockName, int stockCode) {
        this.stockName = stockName;
        this.stockCode = stockCode;
    }
}
