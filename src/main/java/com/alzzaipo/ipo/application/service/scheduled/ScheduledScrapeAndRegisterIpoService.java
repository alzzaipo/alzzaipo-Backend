package com.alzzaipo.ipo.application.service.scheduled;

import com.alzzaipo.ipo.application.port.in.ScrapeAndRegisterIposUseCase;
import com.alzzaipo.ipo.application.port.out.dto.ScrapeIposCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledScrapeAndRegisterIpoService {

    private final ScrapeAndRegisterIposUseCase scrapeAndRegisterIposUseCase;

    @Scheduled(cron = "0 0 2 ? * MON-FRI")
    private void scheduledTask() {
        ScrapeIposCommand command = new ScrapeIposCommand(1, 1);

        scrapeAndRegisterIposUseCase.scrapeAndRegisterIposUseCase(command);
    }
}
