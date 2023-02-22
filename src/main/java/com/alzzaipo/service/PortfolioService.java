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

    public Portfolio findPortfolioById(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 Portfolio가 없습니다. id=" + id));

        return portfolio;
    }

    public void delete(Portfolio portfolio) {
        portfolioRepository.delete(portfolio);
    }

    public Portfolio save(Portfolio portfolio) {
        if(portfolio.getId() != null) {
            return em.merge(portfolio);
        } else {
            em.persist(portfolio);
        }

        return portfolio;
    }

    public Portfolio createPortfolio(PortfolioSaveRequestDto requestDto) {
        IPO ipo = ipoRepository.findByStockCode(requestDto.getStockCode())
                .orElseThrow(() -> new IllegalArgumentException("해당 IPO가 없습니다. stockCode=" + requestDto.getStockCode()));

        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 Member가 없습니다. memberId=" + requestDto.getMemberId()));

        Portfolio portfolio = Portfolio.builder()
                .member(member)
                .ipo(ipo)
                .sharesCnt(requestDto.getSharesCnt())
                .profit(requestDto.getProfit())
                .agents(requestDto.getAgents())
                .memo(requestDto.getMemo())
                .build();

        member.addPortfolio(portfolio);

        return portfolioRepository.save(portfolio);
    }

    @Transactional(readOnly = true)
    public List<PortfolioListDto> getPortfolioListDtosByMemberId(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Member를 찾을 수 없습니다. id=" + memberId));

        return member.getPortfolios().stream()
                .map(Portfolio::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Portfolio> getPortfolioListByMemberId(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 Member를 찾을 수 없습니다. id=" + memberId));

        return member.getPortfolios();
    }

    @Transactional(readOnly = true)
    public Optional<PortfolioSaveRequestDto> getPortfolioSaveRequestDto(Long portfolioId) {
        return portfolioRepository.getPortfolioSaveRequestDto(portfolioId);
    }

    public Portfolio fromSaveRequestDtoToEntity(PortfolioSaveRequestDto portfolioSaveRequestDto) {
        if(portfolioSaveRequestDto == null) {
            log.error("PortfolioSaveRequestDto is null");
            return null;
        }

        Optional<IPO> ipo = ipoRepository.findByStockCode(portfolioSaveRequestDto.getStockCode());
        if(ipo.isEmpty()) {
            return null;
        }

        Optional<Member> member = memberRepository.findById(portfolioSaveRequestDto.getMemberId());
        if(member.isEmpty()){
            return null;
        }

        Portfolio portfolio = Portfolio.builder()
                .member(member.get())
                .ipo(ipo.get())
                .sharesCnt(portfolioSaveRequestDto.getSharesCnt())
                .profit(portfolioSaveRequestDto.getProfit())
                .agents(portfolioSaveRequestDto.getAgents())
                .memo(portfolioSaveRequestDto.getMemo())
                .build();
        
        portfolio.changeId(portfolioSaveRequestDto.getPortfolioId());
        System.out.println("portfolio.getId() = " + portfolio.getId());
        return portfolio;
    }
}
