package com.alzzaipo.hexagonal.ipo.application.port.in;

import com.alzzaipo.hexagonal.ipo.application.port.out.ScrapeIposCommand;

public interface ScrapeAndRegisterIposUseCase {

    int scrapeAndRegisterIposUseCase(ScrapeIposCommand scrapeIposCommand);
}
