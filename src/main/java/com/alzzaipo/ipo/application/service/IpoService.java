package com.alzzaipo.ipo.application.service;

import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.ipo.IpoMapper;
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
import com.alzzaipo.ipo.application.port.out.dto.ScrapeIpoResult;
import com.alzzaipo.ipo.application.port.out.dto.ScrapeIposCommand;
import com.alzzaipo.ipo.domain.Ipo;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IpoService implements GetIpoListQuery,
    GetIpoAgentListQuery,
    AnalyzeIpoProfitRateQuery,
    ScrapeAndRegisterIposUseCase {

    private final IpoPersistenceAdapter ipoPersistenceAdapter;
    private final FindIpoByStockCodePort findIpoByStockCodePort;
    private final FindAnalyzeIpoProfitRateTargetPort findAnalyzeIpoProfitRateTargetPort;
    private final ScrapeIposPort scrapeIposPort;
    private final RegisterIpoPort registerIpoPort;
    private final IpoMapper ipoMapper;

    @Override
    public List<Ipo> getIpoList() {
        return ipoPersistenceAdapter.findIpoList();
    }

    @Override
    public List<String> getIpoAgentList(int stockCode) {
        String separator = ",";

        Ipo ipo = findIpoByStockCodePort.findIpoByStockCodePort(stockCode)
            .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "종목코드 조회 실패"));

        if (ipo.getAgents() == null || ipo.getAgents().isBlank()) {
            throw new CustomException(HttpStatus.NOT_FOUND, "주관사 조회 실패");
        }

        return Arrays.asList(ipo.getAgents().split(separator));
    }

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

    @Override
    public int scrapeAndRegisterIposUseCase(ScrapeIposCommand scrapeIposCommand) {
        List<ScrapeIpoResult> scrapeIpoResults = scrapeIposPort.scrapeIpos(scrapeIposCommand);

        return (int) scrapeIpoResults.stream()
            .filter(this::isNotRegistered)
            .map(ipoMapper::toDomainEntity)
            .peek(registerIpoPort::registerIpo)
            .count();
    }

    private boolean isNotRegistered(ScrapeIpoResult scrapeIpoResult) {
        int stockCode = scrapeIpoResult.getStockCode();

        return findIpoByStockCodePort.findIpoByStockCodePort(stockCode)
            .isEmpty();
    }
}
