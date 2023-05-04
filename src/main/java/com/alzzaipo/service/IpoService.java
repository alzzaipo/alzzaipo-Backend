package com.alzzaipo.service;

import com.alzzaipo.domain.dto.IpoAnalyzeRequestDto;
import com.alzzaipo.domain.dto.IpoAnalyzeResponseDto;
import com.alzzaipo.domain.dto.IpoDto;
import com.alzzaipo.domain.dto.IpoListDto;
import com.alzzaipo.domain.ipo.Ipo;
import com.alzzaipo.domain.ipo.IpoRepository;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class IpoService {

    private final IpoRepository ipoRepository;

    @Transactional
    public void save(Ipo ipo) {
        // 이미 등록된 공모주인 경우 저장하지 않습니다.
        if(ipoRepository.findByStockCode(ipo.getStockCode()).isEmpty()) {
            ipoRepository.save(ipo);
        }
    }

    // 데이터베이스의 모든 공모주에 대해 (종목명, 종목코드) 정보를 반환합니다
    public List<IpoListDto> getIpoList() {
        List<IpoListDto> ipoList = ipoRepository.findAllIpoListDto();
        return ipoList;
    }

    // Ipo의 식별자인 종목코드(stockCode)로 조회하여 해당하는 엔티티를 반환합니다
    public Ipo findByStockCode(int stockCode) {
        Ipo ipo = ipoRepository.findByStockCode(stockCode)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_STOCKCODE, "종목코드 조회 실패: " + stockCode));
        return ipo;
    }

    // [조회년도, 최소 기관경쟁률, 최소 의무보유확약비율] 조건에 부합하는 공모주의 목록과 평균 수익률을 담은 응답 DTO를 반환합니다
    public IpoAnalyzeResponseDto analyze(IpoAnalyzeRequestDto ipoAnalyzeRequestDto) {

        int from = ipoAnalyzeRequestDto.getFrom();
        int to = ipoAnalyzeRequestDto.getTo();
        int minCompetitionRate = ipoAnalyzeRequestDto.getMinCompetitionRate();
        int minLockupRate = ipoAnalyzeRequestDto.getMinLockupRate();

        // 인자값 검사
        if(!(from >= 0 && from < 10000 && to >= 0 && to < 10000)) {
            throw new AppException(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION, "조회년도가 올바르지 않습니다(범위 : 0~9999");
        }
        if(!(from <= to)) {
            throw new AppException(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION, "from의 값이 to보다 큽니다");
        }
        if(!(minCompetitionRate >= 0 && minCompetitionRate < 10000)) {
            throw new AppException(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION, "기관경쟁률이 올바르지 않습니다(범위 : 0~9999");
        }
        if(!(minLockupRate >= 0 && minLockupRate <= 100)) {
            throw new AppException(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION, "확약비율이 올바르지 않습니다(범위 : 0~100)");
        }

        // (조회년도, 최소 기관경쟁률, 최소 확약비율) 조건에 부합하는 공모주 목록 조회
        LocalDate dateFrom = LocalDate.of(from, 1, 1);
        LocalDate dateTo = LocalDate.of(to, 12, 31);
        List<Ipo> ipoList = ipoRepository.analyze(dateFrom, dateTo, minCompetitionRate, minLockupRate);

        // 해당하는 공모주가 없는 경우 (평균 수익률:0, 공모주 목록:empty)를 반환
        if(ipoList.isEmpty()) {
            return new IpoAnalyzeResponseDto(0, new ArrayList<>());
        }

        // 응답 DTO에 포함할 평균 수익률과 공모주 목록
        int sumOfProfitRate = 0, averageProfitRate = 0;
        List<IpoDto> ipoDtoList = new ArrayList<>();

        // Ipo 엔티티를 DTO로 변환하여 리스트에 저장
        for (Ipo ipo : ipoList) {
            ipoDtoList.add(ipo.toDto());
            sumOfProfitRate += ipo.getProfitRate();
        }

        // 평균 수익률 계산
        averageProfitRate = sumOfProfitRate / ipoList.size();

        // (평균 수익률, 공모주 목록) 정보를 담은 응답 DTO 생성
        IpoAnalyzeResponseDto ipoAnalyzeResponseDto = new IpoAnalyzeResponseDto(averageProfitRate, ipoDtoList);

        // 응답 DTO 반환
        return ipoAnalyzeResponseDto;
    }

}
