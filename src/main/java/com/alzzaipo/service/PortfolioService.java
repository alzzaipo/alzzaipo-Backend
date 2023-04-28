package com.alzzaipo.service;

import com.alzzaipo.domain.dto.PortfolioListResponseDto;
import com.alzzaipo.domain.dto.PortfolioUpdateRequestDto;
import com.alzzaipo.domain.ipo.Ipo;
import com.alzzaipo.domain.ipo.IpoRepository;
import com.alzzaipo.domain.member.Member;
import com.alzzaipo.domain.member.MemberRepository;
import com.alzzaipo.domain.portfolio.Portfolio;
import com.alzzaipo.domain.portfolio.PortfolioRepository;
import com.alzzaipo.domain.dto.PortfolioCreateRequestDto;
import com.alzzaipo.exception.AppException;
import com.alzzaipo.exception.ErrorCode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class PortfolioService {

    private final EntityManager em;
    private final PortfolioRepository portfolioRepository;
    private final IpoRepository ipoRepository;
    private final MemberRepository memberRepository;

    public Portfolio save(Portfolio portfolio) {
        // 생성 요청이면 save로 신규 저장
        if(portfolio.getId() == null) {
            em.persist(portfolio);
            return portfolio;
        }
        // 수정 요청이면 merge로 수정 저장
        else {
            return em.merge(portfolio);
        }
    }

    public Portfolio createMemberPortfolio(String accountId, PortfolioCreateRequestDto createRequestDto) {

        // 계정 ID 검증
        Member member = getMemberAfterValidation(accountId);

        // 종목코드 검증
        Ipo ipo = getIpoAfterValidation(createRequestDto.getStockCode());

        // DTO로부터 새로운 포트폴리오 생성
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

    public Portfolio updateMemberPortfolio(String accountId, PortfolioUpdateRequestDto portfolioUpdateRequestDto) {
        // 계정 ID 검증
        Member member = getMemberAfterValidation(accountId);

        // 포트폴리오 ID 검증
        Portfolio oldPortfolio = getPortfolioAfterValidation(portfolioUpdateRequestDto.getPortfolioId());

        // 종목코드 검증
        Ipo ipo = getIpoAfterValidation(portfolioUpdateRequestDto.getStockCode());

        // 포트폴리오 소유권 검증
        validatePortfolioOwnership(accountId, oldPortfolio.getMember().getAccountId());

        // 수정된 내용의 포트폴리오 생성
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
        save(newPortfolio);

        return newPortfolio;
    }

    public boolean deleteMemberPortfolio(String accountId, Long portfolioId) {
        // 계정 ID 검증
        Member member = getMemberAfterValidation(accountId);

        // 포트폴리오 ID 검증
        Portfolio portfolio = getPortfolioAfterValidation(portfolioId);

        // 포트폴리오 소유권 검증
        validatePortfolioOwnership(accountId, portfolio.getMember().getAccountId());

        // 포트폴리오 삭제
        portfolioRepository.delete(portfolio);

        return true;
    }

    /* 회원의 포트폴리오 목록 반환 */
    @Transactional(readOnly = true)
    public List<PortfolioListResponseDto> getMemberPortfolioList(String accountId) {
        // 계정 ID 검증
        Member member = getMemberAfterValidation(accountId);

        // 포트폴리오를 DTO로 변환하여 반환
        return member.getPortfolios().stream()
                    .map(Portfolio::toDto)
                    .collect(Collectors.toList());
    }

    // 계정 ID 검증 후, 해당하는 Member Entity 반환
    private Member getMemberAfterValidation(String accountId) {
        Member member = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_ID_NOT_FOUND,
                        accountId + ": 존재하지 않는 회원입니다."));
        return member;
    }

    // 종목코드 검증 후, 해당하는 IPO Entity 반환
    private Ipo getIpoAfterValidation(int stockCode) {
        Ipo ipo = ipoRepository.findByStockCode(stockCode)
                .orElseThrow(() -> new AppException(ErrorCode.IPO_NOT_FOUND,
                        stockCode + ": 존재하지 않는 종목코드 입니다"));
        return ipo;
    }

    // 포트폴리오 ID 검증 후, 해당하는 Portfolio Entity 반환
    private Portfolio getPortfolioAfterValidation(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new AppException(ErrorCode.PORTFOLIO_NOT_FOUND, "존재하지 않는 포트폴리오 입니다."));
        return portfolio;
    }

    // 회원이 포트폴리오의 생성자인지 검증
    private void validatePortfolioOwnership(String accountId, String portfolioOwnerAccountId) {
        if(!accountId.equals(portfolioOwnerAccountId)) {
            throw new AppException(ErrorCode.INVALID_ACCOUNT_ID, "해당 회원의 포트폴리오가 아닙니다.");
        }
    }

}
