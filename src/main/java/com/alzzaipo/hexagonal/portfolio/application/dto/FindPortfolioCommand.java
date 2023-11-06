package com.alzzaipo.hexagonal.portfolio.application.dto;

import com.alzzaipo.hexagonal.common.Uid;
import lombok.Getter;

@Getter
public class FindPortfolioCommand {

    private final Uid memberUID;
    private final Uid portfolioUID;

    public FindPortfolioCommand(Uid memberUID, Uid portfolioUID) {
        this.memberUID = memberUID;
        this.portfolioUID = portfolioUID;
    }
}
