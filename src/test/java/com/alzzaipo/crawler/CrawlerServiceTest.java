package com.alzzaipo.crawler;

import com.alzzaipo.domain.ipo.Ipo;
import com.alzzaipo.domain.ipo.IpoRepository;
import com.alzzaipo.dto.CrawlerDto;
import com.alzzaipo.service.CrawlerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class CrawlerServiceTest {

    private final IpoRepository ipoRepository;
    private final CrawlerService crawlerService;

    @Autowired
    public CrawlerServiceTest(IpoRepository ipoRepository, CrawlerService crawlerService) {
        this.ipoRepository = ipoRepository;
        this.crawlerService = crawlerService;
    }

    @Test
    public void getCrawlerDtoListFromPage() {
        //given
        int pageNumber = 1;

        //when
        List<CrawlerDto> crawlerDtoList = crawlerService.getCrawlerDtoListFromPage(pageNumber);

        //then
        for (CrawlerDto crawlerDto : crawlerDtoList) {
            System.out.println(crawlerDto.toString());
        }

        assertThat(crawlerDtoList.size()).isEqualTo(20);
    }

    @Test
    public void getIPOListFromPage() throws Exception
    {
        //given
        int pageNumber = 1;

        //when
        List<Ipo> ipoList = crawlerService.getIPOListFromPage(pageNumber);

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
        int from = 2023;

        //when
        crawlerService.updateIPOListFrom(from);

        //then
        List<Ipo> findAllList = ipoRepository.findAll();
        assertThat(findAllList.size()).isGreaterThan(0);
        System.out.println(findAllList.size());
    }

}