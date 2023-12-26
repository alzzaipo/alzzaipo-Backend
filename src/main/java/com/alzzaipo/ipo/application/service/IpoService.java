package com.alzzaipo.ipo.application.service;

import com.alzzaipo.ipo.adapter.out.persistence.IpoPersistenceAdapter;
import com.alzzaipo.ipo.application.port.in.AnalyzeIpoProfitRateQuery;
import com.alzzaipo.ipo.application.port.in.GetIpoAgentListQuery;
import com.alzzaipo.ipo.application.port.in.GetIpoListQuery;
import com.alzzaipo.ipo.application.port.in.ScrapeAndRegisterIposUseCase;
import com.alzzaipo.ipo.application.port.in.dto.AnalyzeIpoProfitRateCommand;
import com.alzzaipo.ipo.application.port.in.dto.AnalyzeIpoProfitRateResult;
import com.alzzaipo.ipo.application.port.out.FindAnalyzeIpoProfitRateTargetPort;
import com.alzzaipo.ipo.application.port.out.FindIpoByStockCodePort;
import com.alzzaipo.ipo.application.port.out.RegisterIpoPort;
import com.alzzaipo.ipo.application.port.out.ScrapeIposPort;
import com.alzzaipo.ipo.application.port.out.dto.CheckIpoRegisteredPort;
import com.alzzaipo.ipo.application.port.out.dto.ScrapeIposCommand;
import com.alzzaipo.ipo.application.port.out.dto.ScrapedIpoDto;
import com.alzzaipo.ipo.domain.Ipo;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IpoService implements GetIpoListQuery,
    GetIpoAgentListQuery,
    AnalyzeIpoProfitRateQuery,
    ScrapeAndRegisterIposUseCase {

    private final IpoPersistenceAdapter ipoPersistenceAdapter;
    private final FindIpoByStockCodePort findIpoByStockCodePort;
    private final FindAnalyzeIpoProfitRateTargetPort findAnalyzeIpoProfitRateTargetsPort;
    private final ScrapeIposPort scrapeIposPort;
    private final RegisterIpoPort registerIpoPort;
    private final CheckIpoRegisteredPort checkIpoRegisteredPort;

    @Override
    public List<Ipo> getIpoList() {
        return ipoPersistenceAdapter.findIpoList();
    }

    @Override
    public List<String> getIpoAgentList(int stockCode) {
        String separator = ",";
        Ipo ipo = findIpoByStockCodePort.findByStockCode(stockCode);
        return Arrays.asList(ipo.getAgents().split(separator));
    }

    @Override
    public AnalyzeIpoProfitRateResult analyzeIpoProfitRate(AnalyzeIpoProfitRateCommand command) {
        List<Ipo> targetIpos = findAnalyzeIpoProfitRateTargetsPort.findTargets(command);

        int averageProfitRate = (int) targetIpos.stream()
            .mapToInt(Ipo::getProfitRate)
            .average()
            .orElse(0.0);

        return new AnalyzeIpoProfitRateResult(averageProfitRate, targetIpos);
    }

    @Override
    public int scrapeAndRegisterIposUseCase(ScrapeIposCommand scrapeIposCommand) {
        return (int) scrapeIposPort.scrapeIpos(scrapeIposCommand)
            .stream()
            .filter(ipoDto -> !checkIpoRegisteredPort.existsByStockCode(ipoDto.getStockCode()))
            .map(ScrapedIpoDto::toDomainEntity)
            .peek(registerIpoPort::registerIpo)
            .count();
    }
}
