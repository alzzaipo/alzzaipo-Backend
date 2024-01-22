package com.alzzaipo.hexagonal.ipo.adapter.out.web;

import com.alzzaipo.ipo.adapter.out.web.ScrapeIposFromIpo38Adapter;
import com.alzzaipo.ipo.application.port.out.dto.ScrapedIpoDto;
import com.alzzaipo.ipo.application.port.out.dto.ScrapeIposCommand;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

@DisplayName("IPO38 스크래핑 테스트")
public class ScrapeIposFromIpo38AdapterTest {

    ScrapeIposFromIpo38Adapter scrapeIposFromIpo38Adapter = new ScrapeIposFromIpo38Adapter();

    @Test
    @DisplayName("한 페이지로부터 20개의 IPO 정보를 스크래핑 한다")
    public void testScrapeIpos() {
        //given
        ScrapeIposCommand scrapeIposCommand = new ScrapeIposCommand(1, 1);

        //when
        List<ScrapedIpoDto> scrapedIpoDtos
                = scrapeIposFromIpo38Adapter.scrape(scrapeIposCommand);

        //then
        Assertions.assertThat(scrapedIpoDtos.size()).isEqualTo(20);
    }

}
