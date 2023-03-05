package com.alzzaipo.service;

import com.alzzaipo.web.domain.IPO.IPO;
import com.alzzaipo.web.domain.IPO.IPORepository;
import com.alzzaipo.web.domain.Member.Member;
import com.alzzaipo.web.domain.Member.MemberRepository;
import com.alzzaipo.web.domain.Portfolio.Portfolio;
import com.alzzaipo.web.domain.Portfolio.PortfolioRepository;
import com.alzzaipo.web.dto.PortfolioCreateRequestDto;
import com.alzzaipo.web.dto.PortfolioListDto;
import com.alzzaipo.web.dto.PortfolioUpdateDto;
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

    public Boolean createMemberPortfolio(Long memberId, PortfolioCreateRequestDto createRequestDto) {
        IPO ipo = ipoRepository.findByStockCode(createRequestDto.getStockCode())
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

    public Boolean updateMemberPortfolio(Long memberId, PortfolioUpdateDto portfolioUpdateDto) {
        if(portfolioUpdateDto == null) {
            log.error("PortfolioSaveRequestDto is null");
            return false;
        }

        if(portfolioRepository.findById(portfolioUpdateDto.getPortfolioId()).isEmpty()) {
            log.error("Invalid PortfolioId - portfolioUpdateDto.getPortfolioId()" + portfolioUpdateDto.getPortfolioId());
            return false;
        }

        IPO ipo = ipoRepository.findByStockCode(portfolioUpdateDto.getStockCode())
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
                    .sharesCnt(portfolioUpdateDto.getSharesCnt())
                    .profit(portfolioUpdateDto.getProfit())
                    .agents(portfolioUpdateDto.getAgents())
                    .memo(portfolioUpdateDto.getMemo())
                    .build();

            portfolio.changeId(portfolioUpdateDto.getPortfolioId());
            save(portfolio);
            return true;
        }
    }

    public boolean deleteMemberPortfolio(Long memberId, Long portfolioId) {
        Portfolio portfolio = findPortfolioById(portfolioId);

        if(portfolio.getId() == null) {
            log.warn("Invalid portfolioId");
            return false;
        }
        else if(memberId.equals(portfolio.getMember().getId())) {
            portfolioRepository.delete(portfolio);
            return true;
        }
        else {
            log.warn("MemberId not match with portfolio owner - memberId:" + memberId + " ownerId:" + portfolio.getMember().getId());
            return false;
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
    public Optional<PortfolioUpdateDto> getPortfolioUpdateDto(Long portfolioId) {
        return portfolioRepository.getPortfolioUpdateDto(portfolioId);
    }
}
