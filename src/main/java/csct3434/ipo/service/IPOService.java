package csct3434.ipo.service;

import csct3434.ipo.web.domain.IPO;
import csct3434.ipo.web.domain.IPORepository;
import csct3434.ipo.web.dto.IPOAnalyzeRequestDto;
import csct3434.ipo.web.dto.IPOAnalyzeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class IPOService {

    private final IPORepository ipoRepository;

    public IPO findByStockCode(int stockCode) {
        return ipoRepository.findByStockCode(stockCode);
    }

    public void save(IPO ipo) {
        if(ipoRepository.findByStockCode(ipo.getStockCode()) == null) {
            ipoRepository.save(ipo);
        }
    }

    public List<IPO> findAll() {
        return ipoRepository.findAll();
    }


    public List<IPOAnalyzeResponseDto> analyze(IPOAnalyzeRequestDto requestDto) {
        List<IPOAnalyzeResponseDto> resposeDtoList = new ArrayList<>();
        LocalDate from = LocalDate.of(requestDto.getFrom(), 1, 1);
        LocalDate to = LocalDate.of(requestDto.getTo(), 12, 31);
        List<IPO> ipoList = ipoRepository.analyze(from, to, requestDto.getCompetitionRate(), requestDto.getLockupRate());

        for (IPO ipo : ipoList) {
            IPOAnalyzeResponseDto responseDto = IPOAnalyzeResponseDto.builder()
                    .stockName(ipo.getStockName())
                    .stockCode(ipo.getStockCode())
                    .expectedOfferingPriceMin(ipo.getExpectedOfferingPriceMin())
                    .expectedOfferingPriceMax(ipo.getExpectedOfferingPriceMax())
                    .fixedOfferingPrice(ipo.getFixedOfferingPrice())
                    .totalAmount(ipo.getTotalAmount())
                    .competitionRate(ipo.getCompetitionRate())
                    .lockupRate(ipo.getLockupRate())
                    .agents(ipo.getAgents())
                    .listedDate(ipo.getListedDate())
                    .initialMarketPrice(ipo.getInitialMarketPrice())
                    .profitRate(ipo.getProfitRate())
                    .build();

            resposeDtoList.add(responseDto);
        }

        return resposeDtoList;
    }

    public int getAverageProfitRateFromIPOAnalyzeResponseDto(List<IPOAnalyzeResponseDto> responseDtoList) {

        int sum = 0;
        for (IPOAnalyzeResponseDto responseDto : responseDtoList) {
            sum += responseDto.getProfitRate();
        }

        if(responseDtoList.size() == 0) {
            return 0;
        }

        return sum / responseDtoList.size();
    }
}
