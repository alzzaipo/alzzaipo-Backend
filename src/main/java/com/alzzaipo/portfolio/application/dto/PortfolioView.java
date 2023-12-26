package com.alzzaipo.portfolio.application.dto;

import com.alzzaipo.common.Id;
import com.alzzaipo.portfolio.domain.Portfolio;
import lombok.Getter;

@Getter
public class PortfolioView {

	private final String id;

	private final String stockName;

	private final int stockCode;

	private final int sharesCnt;

	private final long profit;

	private final long profitRate;

	private final String agents;

	private final String memo;

	private PortfolioView(Id id, String stockName, int stockCode, int sharesCnt, long profit, long profitRate, String agents,
		String memo) {
		this.id = id.toString();
		this.stockName = stockName;
		this.stockCode = stockCode;
		this.sharesCnt = sharesCnt;
		this.profit = profit;
		this.profitRate = profitRate;
		this.agents = agents;
		this.memo = memo;
	}

	public static PortfolioView build(Portfolio portfolio) {
		return new PortfolioView(portfolio.getPortfolioId(),
			portfolio.getStockName(),
			portfolio.getStockCode(),
			portfolio.getSharesCnt(),
			portfolio.getProfit(),
			portfolio.getProfitRate(),
			portfolio.getAgents(),
			portfolio.getMemo());
	}
}
