package com.alzzaipo.hexagonal.portfolio.adapter.out.persistence;

import com.alzzaipo.hexagonal.ipo.adapter.out.persistence.IpoJpaEntity;
import com.alzzaipo.hexagonal.ipo.adapter.out.persistence.NewIpoRepository;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.member.NewMemberRepository;
import com.alzzaipo.hexagonal.portfolio.application.out.RegisterPortfolioPort;
import com.alzzaipo.hexagonal.portfolio.domain.Portfolio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class PortfolioPersistenceAdapter implements RegisterPortfolioPort {

    private final NewPortfolioRepository portfolioRepository;
    private final NewIpoRepository ipoRepository;
    private final NewMemberRepository memberRepository;

    @Override
    public void registerPortfolio(Portfolio portfolio) {
        MemberJpaEntity memberJpaEntity = memberRepository.findByUid(portfolio.getMemberUID().get())
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));

        IpoJpaEntity ipoJpaEntity = ipoRepository.findByStockCode(portfolio.getStockCode())
                .orElseThrow(() -> new RuntimeException("공모주 조회 실패"));

        PortfolioJpaEntity portfolioJpaEntity = toJpaEntity(
                memberJpaEntity,
                ipoJpaEntity,
                portfolio);

        portfolioRepository.save(portfolioJpaEntity);
    }

    private PortfolioJpaEntity toJpaEntity(MemberJpaEntity memberJpaEntity,
                                           IpoJpaEntity ipoJpaEntity,
                                           Portfolio portfolio) {
        return new PortfolioJpaEntity(
                portfolio.getPortfolioUID().get(),
                portfolio.getSharesCnt(),
                portfolio.getProfit(),
                portfolio.getProfitRate(),
                portfolio.getAgents(),
                portfolio.getMemo(),
                memberJpaEntity,
                ipoJpaEntity);
    }
}
