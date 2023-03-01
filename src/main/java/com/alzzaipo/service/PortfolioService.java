package com.alzzaipo.service;

import com.alzzaipo.web.domain.IPO.IPO;
import com.alzzaipo.web.domain.IPO.IPORepository;
import com.alzzaipo.web.domain.Member.Member;
import com.alzzaipo.web.domain.Member.MemberRepository;
import com.alzzaipo.web.domain.Portfolio.Portfolio;
import com.alzzaipo.web.domain.Portfolio.PortfolioRepository;
import com.alzzaipo.web.dto.PortfolioSaveRequestDto;
import com.alzzaipo.web.dto.PortfolioListDto;
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

    public Portfolio findPortfolioById(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseGet(() -> new Portfolio());

        return portfolio;
    }

    public Boolean createMemberPortfolio(Long memberId, PortfolioSaveRequestDto saveRequestDto) {
        IPO ipo = ipoRepository.findByStockCode(saveRequestDto.getStockCode())
                .orElseGet(() -> new IPO());

        Member member = memberRepository.findById(memberId)
                .orElseGet(() -> new Member());

        if(ipo.getId() == null) {
            log.warn("invalid IPO");
            return false;
        }
        else if(member.getId() == null) {
            log.warn("invalid memberId");
            return false;
        }
        else {
            Portfolio portfolio = Portfolio.builder()
                    .member(member)
                    .ipo(ipo)
                    .sharesCnt(saveRequestDto.getSharesCnt())
                    .profit(saveRequestDto.getProfit())
                    .agents(saveRequestDto.getAgents())
                    .memo(saveRequestDto.getMemo())
                    .build();

            member.addPortfolio(portfolio);

            portfolioRepository.save(portfolio);

            return true;
        }
    }

    public Boolean updateMemberPortfolio(Long memberId, PortfolioSaveRequestDto portfolioSaveRequestDto) {
        if(portfolioSaveRequestDto == null) {
            log.error("PortfolioSaveRequestDto is null");
            return false;
        }

        IPO ipo = ipoRepository.findByStockCode(portfolioSaveRequestDto.getStockCode())
                .orElseGet(() -> new IPO());

        Member member = memberRepository.findById(memberId)
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
                    .sharesCnt(portfolioSaveRequestDto.getSharesCnt())
                    .profit(portfolioSaveRequestDto.getProfit())
                    .agents(portfolioSaveRequestDto.getAgents())
                    .memo(portfolioSaveRequestDto.getMemo())
                    .build();

            portfolio.changeId(portfolioSaveRequestDto.getPortfolioId());
            save(portfolio);
            return true;
        }
    }

    public boolean deleteMemberPortfolio(Long memberId, Portfolio portfolio) {
        if(portfolio.getId() == null) {
            log.warn("invalid portfolioId");
            return false;
        }
        else if(memberId == portfolio.getMember().getId()) {
            log.warn("memberId not match with portfolio owner - memberId:" + memberId + " ownerId:" + portfolio.getMember().getId());
            return false;
        }
        else {
            portfolioRepository.delete(portfolio);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public List<PortfolioListDto> getMemberPortfolioListDtos(Long memberId) {

        Member member = memberRepository.findById(memberId)
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
    public Optional<PortfolioSaveRequestDto> getPortfolioSaveRequestDto(Long portfolioId) {
        return portfolioRepository.getPortfolioSaveRequestDto(portfolioId);
    }
}
