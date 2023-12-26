package com.alzzaipo.portfolio.domain;

import com.alzzaipo.common.Id;
import com.alzzaipo.ipo.domain.Ipo;
import com.alzzaipo.portfolio.application.dto.UpdatePortfolioCommand;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Portfolio {

	private final Id portfolioId;

	private final Id memberId;

	private String stockName;

	private int stockCode;

	private int sharesCnt;

	private long profit;

	private long profitRate;

	private String agents;

	private String memo;

	public Portfolio(Id portfolioId, Id memberId, String stockName, int stockCode, int sharesCnt, long profit,
		long profitRate, String agents, String memo) {
		this.portfolioId = portfolioId;
		this.memberId = memberId;
		this.stockName = stockName;
		this.stockCode = stockCode;
		this.sharesCnt = sharesCnt;
		this.profit = profit;
		this.profitRate = profitRate;
		this.agents = agents;
		this.memo = memo;
	}

	public void updatePortfolio(UpdatePortfolioCommand command, Ipo ipo) {
		this.stockName = ipo.getStockName();
		this.stockCode = ipo.getStockCode();
		this.sharesCnt = command.getSharesCnt();
		this.profit = command.getProfit();
		this.profitRate = calculateProfitRate(command.getProfit(), command.getSharesCnt(), ipo.getFixedOfferingPrice());
		this.agents = command.getAgents();
		this.memo = command.getMemo();
	}

	public static Portfolio build(Id memberId, Ipo ipo, long profit, int sharesCount, String agents, String memo) {
		return new Portfolio(Id.generate(),
			memberId,
			ipo.getStockName(),
			ipo.getStockCode(),
			sharesCount,
			profit,
			calculateProfitRate(profit, sharesCount, ipo.getFixedOfferingPrice()),
			agents,
			memo);
	}

	private static long calculateProfitRate(long totalProfit, int sharesCount, int fixedOfferingPrice) {
		if(sharesCount == 0 || fixedOfferingPrice == 0) {
			return 0L;
		}
		return (100 * (totalProfit / sharesCount)) / fixedOfferingPrice;
	}
}
