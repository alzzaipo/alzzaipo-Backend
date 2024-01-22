package com.alzzaipo.ipo.application.port.in;

import com.alzzaipo.ipo.application.port.out.dto.ScrapeIposCommand;

public interface ScrapeAndRegisterIposUseCase {

    int scrapeAndRegister(ScrapeIposCommand command);
}
