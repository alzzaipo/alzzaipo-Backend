package com.alzzaipo.ipo.application.service.scheduled;

import com.alzzaipo.ipo.application.port.in.ScrapeAndRegisterIposUseCase;
import com.alzzaipo.ipo.application.port.out.dto.ScrapeIposCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ScrapeAndRegisterIposService {

    private final ScrapeAndRegisterIposUseCase scrapeAndRegisterIposUseCase;

    @Scheduled(cron = "0 0 2 ? * MON-FRI")
    public void scheduledTask() {
        ScrapeIposCommand command = new ScrapeIposCommand(1, 1);
        scrapeAndRegisterIposUseCase.scrapeAndRegister(command);
    }
}
