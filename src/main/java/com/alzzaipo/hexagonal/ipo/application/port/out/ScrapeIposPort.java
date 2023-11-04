package com.alzzaipo.hexagonal.ipo.application.port.out;

import com.alzzaipo.hexagonal.ipo.application.port.out.dto.ScrapeIpoResult;
import com.alzzaipo.hexagonal.ipo.application.port.out.dto.ScrapeIposCommand;

import java.util.List;

public interface ScrapeIposPort {

    List<ScrapeIpoResult> scrapeIpos(ScrapeIposCommand scrapeIposCommand);
}
