package com.alzzaipo.portfolio.adapter.in.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterPortfolioWebRequest {

    private int stockCode;
    private int sharesCnt;
    private Long profit;
    private String agents;
    private String memo;
}
