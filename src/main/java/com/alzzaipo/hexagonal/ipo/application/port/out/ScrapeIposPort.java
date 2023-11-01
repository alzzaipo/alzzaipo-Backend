package com.alzzaipo.hexagonal.ipo.application.port.out;

import java.util.List;

public interface ScrapeIposPort {

    List<ScrapeIpoResult> scrapeIpos(ScrapeIposCommand scrapeIposCommand);
}
