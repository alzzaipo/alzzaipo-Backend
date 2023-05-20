package com.alzzaipo.dto.portfolio;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PortfolioListDto {
    private Long totalProfit;
    private int totalProfitRate;
    private List<PortfolioDto> portfolioList;
}
