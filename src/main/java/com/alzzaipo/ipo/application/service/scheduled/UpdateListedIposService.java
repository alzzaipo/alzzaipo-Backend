package com.alzzaipo.ipo.application.service.scheduled;

import com.alzzaipo.ipo.application.port.out.FindNotListedIposPort;
import com.alzzaipo.ipo.application.port.out.QueryInitialMarketPricePort;
import com.alzzaipo.ipo.application.port.out.UpdateListedIpoPort;
import com.alzzaipo.ipo.application.port.out.dto.QueryInitialMarketPriceResult;
import com.alzzaipo.ipo.application.port.out.dto.UpdateListedIpoCommand;
import com.alzzaipo.ipo.domain.Ipo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UpdateListedIposService {

    private final FindNotListedIposPort findNotListedIposPort;
    private final QueryInitialMarketPricePort queryInitialMarketPricePort;
    private final UpdateListedIpoPort updateListedIpoPort;

    @Scheduled(cron = "0 0 18 ? * MON-FRI")
    private void updateListedIpos() {
        LocalDate currentDate = LocalDate.now();

        findNotListedIposPort.findNotListedIpos()
                .stream()
                .filter(ipo -> currentDate.isAfter(ipo.getListedDate()))
                .forEach(this::queryInitialMarketPriceAndUpdate);
    }

    private void queryInitialMarketPriceAndUpdate(Ipo ipo) {
        int initialMarketPrice = getInitialMarketPrice(ipo);

        if (initialMarketPrice != -1) {
            updateIpoWithInitialMarketPrice(ipo, initialMarketPrice);
        }
    }

    private int getInitialMarketPrice(Ipo ipo) {
        QueryInitialMarketPriceResult result = queryInitialMarketPricePort.queryInitialMarketPrice(
                ipo.getStockCode(),
                ipo.getListedDate());

        return result.isSuccess() ? result.getInitialMarketPrice() : -1;
    }

    private void updateIpoWithInitialMarketPrice(Ipo ipo, int initialMarketPrice) {
        int fixedOfferingPrice = ipo.getFixedOfferingPrice();
        int profitRate = (int) (((double) (initialMarketPrice - fixedOfferingPrice) / fixedOfferingPrice) * 100);

        UpdateListedIpoCommand command = new UpdateListedIpoCommand(
                ipo.getStockCode(),
                initialMarketPrice,
                profitRate);

        updateListedIpoPort.updateListedIpo(command);
    }

}
