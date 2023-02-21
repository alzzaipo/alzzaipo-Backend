package csct3434.ipo.web.domain.IPO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class IPORepositoryTest {

    @Autowired
    IPORepository ipoRepository;

    @Test
    public void 저장() throws Exception
    {
        //given
        IPO ipo = IPO.builder()
                .stockName("유령회사")
                .stockCode(123)
                .build();

        //when
        ipoRepository.save(ipo);

        //then
        assertThat(ipoRepository.findAll().get(0).getStockName()).isEqualTo("유령회사");
    }

    @Test
    public void 조회() throws Exception
    {
        //given
        IPO ipo = IPO.builder()
                .stockName("유령회사")
                .stockCode(123)
                .build();

        ipoRepository.save(ipo);

        //when
        IPO result = ipoRepository.findById(ipo.getId()).get();

        //then
        assertThat(result).isSameAs(ipo);
        assertThat(result.getStockName()).isEqualTo("유령회사");
    }

    @Test
    public void 삭제() throws Exception
    {
        //given
        IPO ipo1 = IPO.builder()
                .stockName("유령회사")
                .stockCode(123)
                .build();

        IPO ipo2 = IPO.builder()
                .stockName("해적회사")
                .stockCode(4546)
                .build();

        ipoRepository.save(ipo1);
        ipoRepository.save(ipo2);

        //when
        ipoRepository.deleteById(ipo1.getId());
        ipoRepository.delete(ipo2);

        //then
        assertThat(ipoRepository.findAll().size()).isEqualTo(0);
    }
}