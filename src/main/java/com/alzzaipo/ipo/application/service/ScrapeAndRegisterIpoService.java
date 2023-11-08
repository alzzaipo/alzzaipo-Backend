package com.alzzaipo.ipo.application.service;

import com.alzzaipo.ipo.IpoMapper;
import com.alzzaipo.ipo.application.port.in.ScrapeAndRegisterIposUseCase;
import com.alzzaipo.ipo.application.port.out.FindIpoByStockCodePort;
import com.alzzaipo.ipo.application.port.out.RegisterIpoPort;
import com.alzzaipo.ipo.application.port.out.ScrapeIposPort;
import com.alzzaipo.ipo.application.port.out.dto.ScrapeIpoResult;
import com.alzzaipo.ipo.application.port.out.dto.ScrapeIposCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapeAndRegisterIpoService implements ScrapeAndRegisterIposUseCase {

    private final ScrapeIposPort scrapeIposPort;
    private final FindIpoByStockCodePort findIpoByStockCodePort;
    private final RegisterIpoPort registerIpoPort;
    private final IpoMapper ipoMapper;

    @Override
    public int scrapeAndRegisterIposUseCase(ScrapeIposCommand scrapeIposCommand) {
        List<ScrapeIpoResult> scrapeIpoResults
                = scrapeIposPort.scrapeIpos(scrapeIposCommand);

        int registeredCnt = (int) scrapeIpoResults.stream()
                .filter(this::isNotRegistered)
                .map(this.ipoMapper::toDomainEntity)
                .peek(registerIpoPort::registerIpo)
                .count();

        return registeredCnt;
    }

    private boolean isNotRegistered(ScrapeIpoResult scrapeIpoResult) {
        int stockCode = scrapeIpoResult.getStockCode();

        return findIpoByStockCodePort
                .findIpoByStockCodePort(stockCode)
                .isEmpty();
    }
}
