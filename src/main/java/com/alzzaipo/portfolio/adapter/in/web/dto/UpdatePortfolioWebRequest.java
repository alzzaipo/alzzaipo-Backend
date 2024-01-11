package com.alzzaipo.portfolio.adapter.in.web.dto;

import com.alzzaipo.common.Id;
import com.alzzaipo.portfolio.application.dto.UpdatePortfolioCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdatePortfolioWebRequest {

    @NotBlank
    private String id;

    @Min(value = 1)
    private int stockCode;

    @Min(value = 0)
    private int sharesCnt;

    private Long profit;

    private String agents;

    private String memo;

    public UpdatePortfolioCommand toCommand(Id memberId) {
        return new UpdatePortfolioCommand(Id.fromString(id), memberId, stockCode, sharesCnt, profit, agents, memo);
    }
}
