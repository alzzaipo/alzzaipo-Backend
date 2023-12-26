package com.alzzaipo.portfolio.application.dto;

import com.alzzaipo.common.Id;
import lombok.Getter;

@Getter
public class FindPortfolioCommand {

    private final Id memberId;
    private final Id portfolioId;

    public FindPortfolioCommand(Id memberId, Id portfolioId) {
        this.memberId = memberId;
        this.portfolioId = portfolioId;
    }
}
