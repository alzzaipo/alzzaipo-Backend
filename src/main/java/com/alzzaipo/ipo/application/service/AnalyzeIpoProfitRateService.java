package com.alzzaipo.ipo.application.service;

import com.alzzaipo.ipo.application.port.in.dto.AnalyzeIpoProfitRateCommand;
import com.alzzaipo.ipo.application.port.in.AnalyzeIpoProfitRateQuery;
import com.alzzaipo.ipo.application.port.in.dto.AnalyzeIpoProfitRateResult;
import com.alzzaipo.ipo.application.port.out.FindAnalyzeIpoProfitRateTargetPort;
import com.alzzaipo.ipo.domain.Ipo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyzeIpoProfitRateService implements AnalyzeIpoProfitRateQuery {

    private final FindAnalyzeIpoProfitRateTargetPort findAnalyzeIpoProfitRateTargetPort;

    @Override
    public AnalyzeIpoProfitRateResult analyzeIpoProfitRate(AnalyzeIpoProfitRateCommand command) {
        List<Ipo> targetIpos =
                findAnalyzeIpoProfitRateTargetPort.findAnalyzeIpoProfitRateTarget(command);

        int averageProfitRate = (int) targetIpos.stream()
                .mapToInt(Ipo::getProfitRate)
                .average()
                .orElse(0.0);

        return new AnalyzeIpoProfitRateResult(averageProfitRate, targetIpos);
    }
}
