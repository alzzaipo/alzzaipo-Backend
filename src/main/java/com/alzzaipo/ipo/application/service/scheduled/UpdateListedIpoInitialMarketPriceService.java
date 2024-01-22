package com.alzzaipo.ipo.application.service.scheduled;

import com.alzzaipo.ipo.application.port.out.FindNotListedIposPort;
import com.alzzaipo.ipo.application.port.out.QueryInitialMarketPricePort;
import com.alzzaipo.ipo.application.port.out.UpdateListedIpoPort;
import com.alzzaipo.ipo.domain.Ipo;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class UpdateListedIpoInitialMarketPriceService {

    private final UpdateListedIpoPort updateListedIpoPort;
    private final FindNotListedIposPort findNotListedIposPort;
    private final QueryInitialMarketPricePort queryInitialMarketPricePort;

    @Scheduled(cron = "0 0 18 ? * MON-FRI")
    public void updateListedIpos() {
        findNotListedIposPort.findNotListedIpos()
            .parallelStream()
            .filter(ipo -> ipo.getListedDate().isBefore(LocalDate.now()))
            .forEach(this::queryInitialMarketPriceAndUpdate);
    }

    private void queryInitialMarketPriceAndUpdate(Ipo ipo) {
        int initialMarketPrice = queryInitialMarketPricePort.query(ipo.getStockCode(), ipo.getListedDate());

        if (initialMarketPrice == -1) {
            log.error("Query Initial Market Price Failed : {}({})", ipo.getStockName(), ipo.getStockCode());
            return;
        }

        ipo.updateInitialMarketPrice(initialMarketPrice);
        updateListedIpoPort.updateListedIpo(ipo);

        log.info("Initial Market Price Updated : {}({}) / {}", ipo.getStockName(), ipo.getStockCode(),
            initialMarketPrice);
    }
}
