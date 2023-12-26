package com.alzzaipo.portfolio.application.dto;

import com.alzzaipo.common.Id;
import lombok.Getter;

@Getter
public class DeletePortfolioCommand {

    private final Id memberId;
    private final Id portfolioId;

    public DeletePortfolioCommand(Id memberId, Id portfolioId) {
        this.memberId = memberId;
        this.portfolioId = portfolioId;
    }
}
