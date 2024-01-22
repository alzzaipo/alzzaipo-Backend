package com.alzzaipo.ipo.application.service;

import com.alzzaipo.ipo.adapter.out.persistence.IpoPersistenceAdapter;
import com.alzzaipo.ipo.application.port.in.AnalyzeIpoProfitRateQuery;
import com.alzzaipo.ipo.application.port.in.GetIpoAgentListQuery;
import com.alzzaipo.ipo.application.port.in.GetIpoListQuery;
import com.alzzaipo.ipo.application.port.in.ScrapeAndRegisterIposUseCase;
import com.alzzaipo.ipo.application.port.in.dto.AnalyzeIpoProfitRateCommand;
import com.alzzaipo.ipo.application.port.in.dto.AnalyzeIpoProfitRateResult;
import com.alzzaipo.ipo.application.port.out.FindAnalyzeIpoProfitRateTargetsPort;
import com.alzzaipo.ipo.application.port.out.FindIpoByStockCodePort;
import com.alzzaipo.ipo.application.port.out.SaveIposPort;
import com.alzzaipo.ipo.application.port.out.ScrapeIposPort;
import com.alzzaipo.ipo.application.port.out.CheckIpoExistsPort;
import com.alzzaipo.ipo.application.port.out.dto.ScrapeIposCommand;
import com.alzzaipo.ipo.application.port.out.dto.ScrapedIpoDto;
import com.alzzaipo.ipo.domain.Ipo;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class IpoService implements
    GetIpoListQuery,
    GetIpoAgentListQuery,
    AnalyzeIpoProfitRateQuery,
    ScrapeAndRegisterIposUseCase {

    private final SaveIposPort saveIposPort;
    private final ScrapeIposPort scrapeIposPort;
    private final CheckIpoExistsPort checkIpoExistsPort;
    private final IpoPersistenceAdapter ipoPersistenceAdapter;
    private final FindIpoByStockCodePort findIpoByStockCodePort;
    private final FindAnalyzeIpoProfitRateTargetsPort findAnalyzeIpoProfitRateTargetsPort;

    @Override
    @Transactional(readOnly = true)
    public List<Ipo> getIpoList() {
        return ipoPersistenceAdapter.findIpoList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getIpoAgentList(int stockCode) {
        Ipo ipo = findIpoByStockCodePort.findByStockCode(stockCode);
        return Arrays.asList(ipo.getAgents().split(","));
    }

    @Override
    @Transactional(readOnly = true)
    public AnalyzeIpoProfitRateResult analyze(AnalyzeIpoProfitRateCommand command) {
        List<Ipo> ipos = findAnalyzeIpoProfitRateTargetsPort.findTargets(command);

        int averageProfitRate = (int) ipos.stream()
            .mapToInt(Ipo::getProfitRate)
            .average()
            .orElse(0.0);

        return new AnalyzeIpoProfitRateResult(averageProfitRate, ipos);
    }

    @Override
    public int scrapeAndRegister(ScrapeIposCommand command) {
        List<Ipo> scrapedIpos = scrapeIposPort.scrape(command)
            .stream()
            .filter(ipoDto -> !checkIpoExistsPort.existsByStockCode(ipoDto.getStockCode()))
            .map(ScrapedIpoDto::toDomainEntity)
            .toList();

        saveIposPort.saveAll(scrapedIpos);

        return scrapedIpos.size();
    }
}
