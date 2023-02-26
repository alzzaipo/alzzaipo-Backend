package com.alzzaipo.service;

import com.alzzaipo.web.domain.IPO.IPO;
import com.alzzaipo.web.dto.IpoAnalyzeRequestDto;
import com.alzzaipo.web.dto.IpoListResponseDto;
import com.alzzaipo.web.domain.IPO.IPORepository;
import com.alzzaipo.web.dto.IpoAnalyzeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class IpoService {

    private final IPORepository ipoRepository;

    public void save(IPO ipo) {
        if(ipoRepository.findByStockCode(ipo.getStockCode()).isEmpty()) {
            ipoRepository.save(ipo);
        }
    }

    public List<IPO> findAll() {
        return ipoRepository.findAll();
    }

    public IPO findByStockCode(int stockCode) {
        IPO ipo = ipoRepository.findByStockCode(stockCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 IPO가 없습니다. stockCode=" + stockCode));

        return ipo;
    }

    public List<IpoAnalyzeResponseDto> analyze(IpoAnalyzeRequestDto requestDto) {
        List<IpoAnalyzeResponseDto> resposeDtoList = new ArrayList<>();
        LocalDate from = LocalDate.of(requestDto.getFrom(), 1, 1);
        LocalDate to = LocalDate.of(requestDto.getTo(), 12, 31);
        List<IPO> ipoList = ipoRepository.analyze(from, to, requestDto.getCompetitionRate(), requestDto.getLockupRate());

        for (IPO ipo : ipoList) {
            IpoAnalyzeResponseDto responseDto = IpoAnalyzeResponseDto.builder()
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

    public int getAverageProfitRateFromIPOAnalyzeResponseDto(List<IpoAnalyzeResponseDto> responseDtoList) {

        int sum = 0;
        for (IpoAnalyzeResponseDto responseDto : responseDtoList) {
            sum += responseDto.getProfitRate();
        }

        if(responseDtoList.size() == 0) {
            return 0;
        }

        return sum / responseDtoList.size();
    }

    public List<IpoListResponseDto> getAllDtoList() {
        List<IPO> all = findAll();

        List<IpoListResponseDto> result = new ArrayList<>();
        for (IPO ipo : all) {
            result.add(new IpoListResponseDto(ipo.getStockName(), ipo.getStockCode()));
        }

        return result;
    }
}
