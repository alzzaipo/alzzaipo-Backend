package com.alzzaipo.service;

import com.alzzaipo.domain.dto.PortfolioListResponseDto;
import com.alzzaipo.domain.dto.PortfolioUpdateResponseDto;
import com.alzzaipo.domain.ipo.IPO;
import com.alzzaipo.domain.ipo.IPORepository;
import com.alzzaipo.domain.member.Member;
import com.alzzaipo.domain.member.MemberRepository;
import com.alzzaipo.domain.portfolio.Portfolio;
import com.alzzaipo.domain.portfolio.PortfolioRepository;
import com.alzzaipo.domain.dto.PortfolioCreateRequestDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class PortfolioService {

    private final EntityManager em;
    private final PortfolioRepository portfolioRepository;
    private final IPORepository ipoRepository;
    private final MemberRepository memberRepository;

    public Portfolio save(Portfolio portfolio) {
        if(portfolio.getId() == null) {
            em.persist(portfolio);
            return portfolio;
        }
        else {
            return em.merge(portfolio);
        }
    }

    public Boolean createMemberPortfolio(String accountId, PortfolioCreateRequestDto createRequestDto) {
        IPO ipo = ipoRepository.findByStockCode(createRequestDto.getStockCode())
                .orElseGet(() -> new IPO());

        Member member = memberRepository.findByAccountId(accountId)
                .orElseGet(() -> new Member());

        if(ipo.getId() == null) {
            log.warn("Invalid IPO");
            return false;
        }
        else if(member.getId() == null) {
            log.warn("Invalid memberId");
            return false;
        }
        else {
            Portfolio portfolio = Portfolio.builder()
                    .member(member)
                    .ipo(ipo)
                    .sharesCnt(createRequestDto.getSharesCnt())
                    .profit(createRequestDto.getProfit())
                    .agents(createRequestDto.getAgents())
                    .memo(createRequestDto.getMemo())
                    .build();

            member.addPortfolio(portfolio);
            portfolioRepository.save(portfolio);
            return true;
        }
    }

    public Boolean updateMemberPortfolio(String accountId, PortfolioUpdateResponseDto portfolioUpdateResponseDto) {
        if(portfolioUpdateResponseDto == null) {
            log.error("PortfolioSaveRequestDto is null");
            return false;
        }

        if(portfolioRepository.findById(portfolioUpdateResponseDto.getPortfolioId()).isEmpty()) {
            log.error("Invalid PortfolioId - portfolioUpdateDto.getPortfolioId()" + portfolioUpdateResponseDto.getPortfolioId());
            return false;
        }

        IPO ipo = ipoRepository.findByStockCode(portfolioUpdateResponseDto.getStockCode())
                .orElseGet(() -> new IPO());

        Member member = memberRepository.findByAccountId(accountId)
                .orElseGet(() -> new Member());

        if(ipo.getId() == null) {
            log.warn("Invalid IPO");
            return false;
        }
        else if(member.getId() == null) {
            log.warn("Invalid memberId");
            return false;
        }
        else {
            Portfolio portfolio = Portfolio.builder()
                    .member(member)
                    .ipo(ipo)
                    .sharesCnt(portfolioUpdateResponseDto.getSharesCnt())
                    .profit(portfolioUpdateResponseDto.getProfit())
                    .agents(portfolioUpdateResponseDto.getAgents())
                    .memo(portfolioUpdateResponseDto.getMemo())
                    .build();

            portfolio.changeId(portfolioUpdateResponseDto.getPortfolioId());
            save(portfolio);
            return true;
        }
    }

    public boolean deleteMemberPortfolio(String accountId, Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseGet(() -> new Portfolio());

        if(portfolio.getId() == null) {
            log.warn("Invalid portfolioId");
            return false;
        }
        else if(accountId.equals(portfolio.getMember().getAccountId())) {
            portfolioRepository.delete(portfolio);
            return true;
        }
        else {
            log.warn("AccountId not match with portfolio owner - accountId:" + accountId + " ownerId:" + portfolio.getMember().getAccountId());
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<PortfolioListResponseDto> getMemberPortfolioListResponseDtos(String accountId) {
        Member member = memberRepository.findByAccountId(accountId)
                .orElseGet(() -> new Member());

        if(member.getId() == null) {
            log.warn("Invalid memberId");
            return new ArrayList<>();
        }
        else {
            return member.getPortfolios().stream()
                    .map(Portfolio::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional(readOnly = true)
    public Optional<PortfolioUpdateResponseDto> getPortfolioUpdateResponseDto(Long portfolioId) {
        return portfolioRepository.getPortfolioUpdateResponseDto(portfolioId);
    }
}
