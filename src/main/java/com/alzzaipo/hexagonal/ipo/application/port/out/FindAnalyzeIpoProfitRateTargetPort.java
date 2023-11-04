package com.alzzaipo.hexagonal.ipo.application.port.out;

import com.alzzaipo.hexagonal.ipo.application.port.in.dto.AnalyzeIpoProfitRateCommand;
import com.alzzaipo.hexagonal.ipo.domain.Ipo;

import java.util.List;

public interface FindAnalyzeIpoProfitRateTargetPort {

    List<Ipo> findAnalyzeIpoProfitRateTarget(AnalyzeIpoProfitRateCommand command);
}
