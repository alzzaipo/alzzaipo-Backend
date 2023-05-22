package com.alzzaipo.service;

import com.alzzaipo.dto.ipo.IpoAnalyzeRequestDto;
import com.alzzaipo.dto.ipo.IpoListDto;
import com.alzzaipo.domain.ipo.Ipo;
import com.alzzaipo.domain.ipo.IpoRepository;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.enums.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
class IpoServiceTest {

    @Autowired
    IpoService ipoService;

    @MockBean
    IpoRepository ipoRepository;

    @Test
    @DisplayName("이미 존재하는 종목코드를 가진 IPO의 저장 요청은 처리되지 않는다")
    public void 종목코드_중복_처리() throws Exception
    {
        // given : 종목코드가 '12345'인 Ipo 엔티티가 저장된 상태에서
        int stockCode = 12345;

        Ipo savedIpo = Ipo.builder()
                .stockName("stock1")
                .stockCode(stockCode)
                .build();

        when(ipoRepository.findByStockCode(anyInt())).thenReturn(Optional.of(savedIpo));

        // when : 동일한 종목코드를 가진 다른 ipo의 저장을 시도할 때
        Ipo newIpo = Ipo.builder()
                .stockName("stock2")
                .stockCode(stockCode)
                .build();

        ipoService.save(newIpo);

        // then : 해당 요청은 무시된다
        assertThat(ipoService.findByStockCode(stockCode).getStockName()).isNotEqualTo(newIpo.getStockName());
        assertThat(ipoService.findByStockCode(stockCode).getStockName()).isEqualTo(savedIpo.getStockName());
    }

    @Test
    @DisplayName("공모주 목록을 정상적으로 반환한다")
    public void 공모주_목록_조회1() throws Exception {
        // given
        List<IpoListDto> ipoListDtos = new ArrayList<>();
        ipoListDtos.add(new IpoListDto("stock1", 111111));
        ipoListDtos.add(new IpoListDto("stock2", 222222));
        ipoListDtos.add(new IpoListDto("stock3", 333333));

        when(ipoRepository.findAllIpoListDto()).thenReturn(ipoListDtos);

        // when
        List<IpoListDto> allIpoListDto = ipoService.getIpoList();

        // then
        assertThat(allIpoListDto.size()).isEqualTo(3);
        assertThat(allIpoListDto.get(0).getStockName()).isEqualTo("stock1");
        assertThat(allIpoListDto.get(0).getStockCode()).isEqualTo(111111);
        assertThat(allIpoListDto.get(1).getStockName()).isEqualTo("stock2");
        assertThat(allIpoListDto.get(1).getStockCode()).isEqualTo(222222);
        assertThat(allIpoListDto.get(2).getStockName()).isEqualTo("stock3");
        assertThat(allIpoListDto.get(2).getStockCode()).isEqualTo(333333);
    }

    @Test
    @DisplayName("없는 종목코드를 조회하면 STOCK_CODE_NOT_FOUND 예외가 발생한다")
    public void 종목코드_조회_1() throws Exception {
        // given
        when(ipoRepository.findByStockCode(anyInt())).thenReturn(Optional.empty());

        try {
            // when
            ipoService.findByStockCode(123456);
            fail("STOCK_CODE_NOT_FOUND 에러가 발생해야 합니다");
        } catch (AppException e){
            // then
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.INVALID_STOCKCODE);
        }
    }

    @Test
    @DisplayName("존재하는 종목코드를 조회 시, 해당하는 IPO 객체를 정상적으로 반환한다")
    public void 종목코드_조회_2() throws Exception {
        // given
        Ipo ipo = Ipo.builder()
                .stockName("stock1")
                .stockCode(111111)
                .build();

        when(ipoRepository.findByStockCode(anyInt())).thenReturn(Optional.of(ipo));

        Ipo result = ipoService.findByStockCode(111111);
        assertThat(result.getStockName()).isEqualTo(ipo.getStockName());
        assertThat(result.getStockCode()).isEqualTo(ipo.getStockCode());
    }

    @Test
    @DisplayName("공모주 분석 - 잘못된 조회년도로 요청 시 ILLEGAL_ARGUMENT_EXCEPTION이 발생한다")
    public void 공모주_분석_1() throws Exception {
        int validYear = 2000, minCompetitionRate = 1500, minLockupRate = 10;

        // given, when : 유효하지 않은 조회년도로 공모주 분석을 요청했을 때
        // from이 9999보다 클 때
        Throwable thrown1 = catchThrowable(() -> ipoService.analyze(new IpoAnalyzeRequestDto(10000, validYear, minCompetitionRate, minLockupRate)));
        // from이 0보다 작을 때
        Throwable thrown2 = catchThrowable(() -> ipoService.analyze(new IpoAnalyzeRequestDto(-1, validYear, minCompetitionRate, minLockupRate)));
        // to가 9999보다 클 때
        Throwable thrown3 = catchThrowable(() -> ipoService.analyze(new IpoAnalyzeRequestDto(validYear, 10000, minCompetitionRate, minLockupRate)));
        // to가 0보다 작을 때
        Throwable thrown4 = catchThrowable(() -> ipoService.analyze(new IpoAnalyzeRequestDto(validYear, -1, minCompetitionRate, minLockupRate)));
        // from이 to보다 클 때
        Throwable thrown5 = catchThrowable(() -> ipoService.analyze(new IpoAnalyzeRequestDto(3000, 2000, minCompetitionRate, minLockupRate)));

        // then : ILLEGAL_ARGUMENT_EXCEPTION이 발생해야 한다
        assertAll(
                () -> assertThat(thrown1).isInstanceOf(AppException.class),
                () -> assertThat(((AppException) thrown1).getErrorCode()).isEqualTo(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION),

                () -> assertThat(thrown2).isInstanceOf(AppException.class),
                () -> assertThat(((AppException) thrown2).getErrorCode()).isEqualTo(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION),

                () -> assertThat(thrown3).isInstanceOf(AppException.class),
                () -> assertThat(((AppException) thrown3).getErrorCode()).isEqualTo(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION),

                () -> assertThat(thrown4).isInstanceOf(AppException.class),
                () -> assertThat(((AppException) thrown4).getErrorCode()).isEqualTo(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION),

                () -> assertThat(thrown5).isInstanceOf(AppException.class),
                () -> assertThat(((AppException) thrown5).getErrorCode()).isEqualTo(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION)
        );
    }

    @Test
    @DisplayName("공모주 분석 시 from이 음수면 BAD_REQUEST로 응답한다")
    public void 공모주_분석_2() throws Exception {
        // given : from이 유효 범위를 벗어났을 때
        int from = -1, to = 2000, minCompetitionRate = 2000, minLockupRate = 10;

        try {
            // when
            ipoService.analyze(new IpoAnalyzeRequestDto(from, to, minCompetitionRate, minLockupRate));
            fail("ILLEGAL_ARGUMENT_EXCEPTION이 발생해야 한다");
        } catch(AppException e) {
            // then
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION);
        }
    }

    @Test
    @DisplayName("공모주 분석 시 to가 10000 이상이면 BAD_REQUEST로 응답한다")
    public void 공모주_분석_3() throws Exception {
        // given : from이 유효 범위를 벗어났을 때
        int from = 2000, to = 10000, minCompetitionRate = 2000, minLockupRate = 10;

        try {
            // when
            ipoService.analyze(new IpoAnalyzeRequestDto(from, to, minCompetitionRate, minLockupRate));
            fail("ILLEGAL_ARGUMENT_EXCEPTION이 발생해야 한다");
        } catch(AppException e) {
            // then
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION);
        }
    }

    @Test
    @DisplayName("공모주 분석 시 to가 음수면 BAD_REQUEST로 응답한다")
    public void 공모주_분석_4() throws Exception {
        // given : from이 유효 범위를 벗어났을 때
        int from = 2000, to = -1, minCompetitionRate = 2000, minLockupRate = 10;

        try {
            // when
            ipoService.analyze(new IpoAnalyzeRequestDto(from, to, minCompetitionRate, minLockupRate));
            fail("ILLEGAL_ARGUMENT_EXCEPTION이 발생해야 한다");
        } catch(AppException e) {
            // then
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION);
        }
    }

}