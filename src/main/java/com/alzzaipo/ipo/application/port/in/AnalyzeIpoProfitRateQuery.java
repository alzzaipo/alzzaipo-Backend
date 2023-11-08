package com.alzzaipo.ipo.application.port.in;

import com.alzzaipo.ipo.application.port.in.dto.AnalyzeIpoProfitRateCommand;
import com.alzzaipo.ipo.application.port.in.dto.AnalyzeIpoProfitRateResult;

public interface AnalyzeIpoProfitRateQuery {

    AnalyzeIpoProfitRateResult analyzeIpoProfitRate(AnalyzeIpoProfitRateCommand command);
}
