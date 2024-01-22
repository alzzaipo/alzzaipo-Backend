package com.alzzaipo.ipo.application.port.out;

import com.alzzaipo.ipo.application.port.out.dto.ScrapedIpoDto;
import com.alzzaipo.ipo.application.port.out.dto.ScrapeIposCommand;

import java.util.List;

public interface ScrapeIposPort {

    List<ScrapedIpoDto> scrape(ScrapeIposCommand scrapeIposCommand);
}
