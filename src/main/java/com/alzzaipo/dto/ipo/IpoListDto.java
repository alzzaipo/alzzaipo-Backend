package com.alzzaipo.dto.ipo;

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
