package com.alzzaipo.hexagonal.ipo.application.port.in;

public interface AnalyzeIpoProfitRateQuery {

    AnalyzeIpoProfitRateResult analyzeIpoProfitRate(AnalyzeIpoProfitRateCommand command);
}
