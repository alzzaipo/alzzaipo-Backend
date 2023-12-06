package com.alzzaipo.portfolio.adapter.in.web.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterPortfolioWebRequest {

	@Min(value = 1)
	private int stockCode;

	@Min(value = 0)
	private int sharesCnt;

	private long profit;

	private String agents;

	private String memo;
}
