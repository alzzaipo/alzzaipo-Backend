package com.alzzaipo.portfolio.application.dto;

import com.alzzaipo.common.Uid;
import lombok.Getter;

@Getter
public class DeletePortfolioCommand {

    private final Uid memberUID;
    private final Uid portfolioUID;

    public DeletePortfolioCommand(Uid memberUID, Uid portfolioUID) {
        this.memberUID = memberUID;
        this.portfolioUID = portfolioUID;
    }
}
