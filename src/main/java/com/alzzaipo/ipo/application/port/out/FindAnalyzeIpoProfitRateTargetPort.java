package com.alzzaipo.ipo.application.port.out;

import com.alzzaipo.ipo.application.port.in.dto.AnalyzeIpoProfitRateCommand;
import com.alzzaipo.ipo.domain.Ipo;

import java.util.List;

public interface FindAnalyzeIpoProfitRateTargetPort {

    List<Ipo> findAnalyzeIpoProfitRateTarget(AnalyzeIpoProfitRateCommand command);
}
