package csct3434.ipo.service;

import csct3434.ipo.web.domain.IPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class IPOServiceTest {

    @Autowired
    IPOService ipoService;

    @Test
    public void 종목코드_중복_에러처리() throws Exception
    {
        //given
        int stockCode = 12345;

        IPO ipo1 = IPO.builder()
                .stockName("회사1")
                .stockCode(stockCode)
                .build();

        IPO ipo2 = IPO.builder()
                .stockName("회사2")
                .stockCode(stockCode)
                .build();

        ipoService.save(ipo1);

        //when
        ipoService.save(ipo2);

        //then
        assertThat(ipoService.findByStockCode(stockCode).getStockName()).isSameAs(ipo1.getStockName());
    }

}