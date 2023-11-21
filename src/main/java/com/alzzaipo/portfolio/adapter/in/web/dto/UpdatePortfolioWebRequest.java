package com.alzzaipo.portfolio.adapter.in.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdatePortfolioWebRequest {

    private String uid;
    private int stockCode;
    private int sharesCnt;
    private Long profit;
    private String agents;
    private String memo;

}
