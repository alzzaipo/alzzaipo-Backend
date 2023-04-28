package com.alzzaipo.crawler;

import com.alzzaipo.domain.ipo.Ipo;
import com.alzzaipo.domain.ipo.IpoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class CrawlerTest {

    private final IpoRepository ipoRepository;
    private final Crawler crawler;

    @Autowired
    public CrawlerTest(IpoRepository ipoRepository, Crawler crawler) {
        this.ipoRepository = ipoRepository;
        this.crawler = crawler;
    }

    @Test
    public void getCrawlerDtoListFromPage() {
        //given
        int pageNumber = 1;

        //when
        List<CrawlerDto> crawlerDtoList = crawler.getCrawlerDtoListFromPage(pageNumber);

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
        List<Ipo> ipoList = crawler.getIPOListFromPage(pageNumber);

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
        crawler.updateIPOListFrom(from);

        //then
        List<Ipo> findAllList = ipoRepository.findAll();
        assertThat(findAllList.size()).isGreaterThan(0);
        System.out.println(findAllList.size());
    }

}