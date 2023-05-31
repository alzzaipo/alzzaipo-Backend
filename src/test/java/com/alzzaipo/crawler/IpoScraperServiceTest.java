package com.alzzaipo.crawler;

import com.alzzaipo.domain.ipo.Ipo;
import com.alzzaipo.domain.ipo.IpoRepository;
import com.alzzaipo.dto.IpoData;
import com.alzzaipo.service.IpoScraperService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class IpoScraperServiceTest {

    private final IpoRepository ipoRepository;
    private final IpoScraperService ipoScraperService;

    @Autowired
    public IpoScraperServiceTest(IpoRepository ipoRepository, IpoScraperService ipoScraperService) {
        this.ipoRepository = ipoRepository;
        this.ipoScraperService = ipoScraperService;
    }

    @Test
    public void getCrawlerDtoListFromPage() {
        //given
        int pageNumber = 1;

        //when
        List<IpoData> ipoDataList = ipoScraperService.scrapeIpoDataFromPage(pageNumber);

        //then
        for (IpoData ipoData : ipoDataList) {
            System.out.println(ipoData.toString());
        }

        assertThat(ipoDataList.size()).isEqualTo(20);
    }

    @Test
    public void getIPOListFromPage() throws Exception
    {
        //given
        int pageNumber = 1;

        //when
        List<Ipo> ipoList = ipoScraperService.extractIpoEntitiesFromPage(pageNumber);

        //then
        for (Ipo ipo : ipoList) {
            System.out.println("ipo = " + ipo.getStockName());
        }

        assertThat(ipoList.size()).isEqualTo(20);
    }

    @Test
    public void updateIPOListFrom() throws Exception
    {
        //given

        //when
        ipoScraperService.updateIpoTableByScrapingPages(1, 1);

        //then
        List<Ipo> findAllList = ipoRepository.findAll();
        assertThat(findAllList.size()).isGreaterThan(0);
        System.out.println(findAllList.size());
    }

}