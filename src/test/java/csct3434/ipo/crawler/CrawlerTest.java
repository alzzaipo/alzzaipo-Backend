package csct3434.ipo.crawler;

import csct3434.ipo.web.domain.IPO.IPO;
import csct3434.ipo.web.domain.IPO.IPORepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class CrawlerTest {

    @Autowired IPORepository ipoRepository;
    @Autowired Crawler crawler;

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
        List<IPO> ipoList = crawler.getIPOListFromPage(pageNumber);

        //then
        for (IPO ipo : ipoList) {
            System.out.println("ipo = " + ipo);
        }

        assertThat(ipoList.size()).isEqualTo(20);
    }

    @Test
    public void updateIPOListUntil() throws Exception
    {
        //given
        int until = 2023;

        //when
        crawler.updateIPOListUntil(until);

        //then
        List<IPO> findAllList = ipoRepository.findAll();
        assertThat(findAllList.size()).isGreaterThan(0);
        System.out.println(findAllList.size());
    }

}