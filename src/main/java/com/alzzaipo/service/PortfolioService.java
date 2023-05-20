package com.alzzaipo.service;

import com.alzzaipo.domain.ipo.Ipo;
import com.alzzaipo.domain.member.Member;
import com.alzzaipo.domain.portfolio.Portfolio;
import com.alzzaipo.domain.portfolio.PortfolioRepository;
import com.alzzaipo.dto.portfolio.PortfolioCreateRequestDto;
import com.alzzaipo.dto.portfolio.PortfolioDto;
import com.alzzaipo.dto.portfolio.PortfolioListDto;
import com.alzzaipo.dto.portfolio.PortfolioUpdateRequestDto;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.exception.ErrorCode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class PortfolioService {

    private final EntityManager em;
    private final PortfolioRepository portfolioRepository;
    private final IpoService ipoService;
    private final MemberService memberService;

    // 생성 요청이면 save로 신규 저장, 수정 요청이면 merge로 수정 저장
    public Portfolio save(Portfolio portfolio) {
        if(portfolio.getId() != null) {
            return em.merge(portfolio);
        } else {
            em.persist(portfolio);
            return portfolio;
        }
    }

    @Transactional(readOnly = true)
    public Portfolio findById(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_PORTFOLIO, "포트폴리오 조회 실패"));
        return portfolio;
    }

    public Portfolio create(Long memberId, PortfolioCreateRequestDto createRequestDto) {
        Member member = memberService.findById(memberId);

        Ipo ipo = ipoService.findByStockCode(createRequestDto.getStockCode());

        // 새로운 포트폴리오 생성
        Portfolio portfolio = Portfolio.builder()
                .member(member)
                .ipo(ipo)
                .sharesCnt(createRequestDto.getSharesCnt())
                .profit(createRequestDto.getProfit())
                .agents(createRequestDto.getAgents())
                .memo(createRequestDto.getMemo())
                .build();

        // 연관정보 추가
        member.addPortfolio(portfolio);

        // 포트폴리오 저장
        return portfolioRepository.save(portfolio);
    }

    public Portfolio update(Long memberId, PortfolioUpdateRequestDto portfolioUpdateRequestDto) {
        Member member = memberService.findById(memberId);

        Portfolio oldPortfolio = findById(portfolioUpdateRequestDto.getPortfolioId());

        Ipo ipo = ipoService.findByStockCode(portfolioUpdateRequestDto.getStockCode());

        // 포트폴리오 소유권 검증
        validatePortfolioOwnership(memberId, oldPortfolio.getMember().getId());

        // 새로운 내용의 포트폴리오 생성
        Portfolio newPortfolio = Portfolio.builder()
                .member(member)
                .ipo(ipo)
                .sharesCnt(portfolioUpdateRequestDto.getSharesCnt())
                .profit(portfolioUpdateRequestDto.getProfit())
                .agents(portfolioUpdateRequestDto.getAgents())
                .memo(portfolioUpdateRequestDto.getMemo())
                .build();

        // 포트폴리오 수정 저장
        newPortfolio.updateId(portfolioUpdateRequestDto.getPortfolioId());
        return save(newPortfolio);
    }

    public boolean delete(Long memberId, Long portfolioId) {
        Member member = memberService.findById(memberId);

        Portfolio portfolio = findById(portfolioId);

        // 포트폴리오 소유권 검증
        validatePortfolioOwnership(memberId, portfolio.getMember().getId());

        // 포트폴리오 삭제
        portfolioRepository.delete(portfolio);

        return true;
    }

    @Transactional(readOnly = true)
    public PortfolioListDto getPortfolioListDto(Long memberId) {
        Long totalProfit = 0L;
        int totalProfitRate = 0;
        List<PortfolioDto> portFolioDtoList = new ArrayList<>();
        List<Portfolio> portfolioList = portfolioRepository.findByMemberId(memberId);

        if(portfolioList.isEmpty()) {
            totalProfit = 0L;
            totalProfitRate = 0;
        }
        else {
            for(Portfolio p : portfolioList) {
                totalProfit += p.getProfit();
                totalProfitRate += p.getProfitRate();
                portFolioDtoList.add(p.toDto());
            }

            totalProfitRate = totalProfitRate / portfolioList.size();
        }

        return new PortfolioListDto(totalProfit, totalProfitRate, portFolioDtoList);
    }

    // 회원이 포트폴리오의 소유자인지 검증
    private void validatePortfolioOwnership(Long memberId, Long ownerId) {
        if(memberId != ownerId) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "해당 포트폴리오에 대한 권한이 없습니다.");
        }
    }
}
