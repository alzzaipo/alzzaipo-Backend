package com.alzzaipo.hexagonal.portfolio.adapter.out.persistence;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.ipo.adapter.out.persistence.IpoJpaEntity;
import com.alzzaipo.hexagonal.ipo.adapter.out.persistence.NewIpoRepository;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.hexagonal.member.adapter.out.persistence.member.NewMemberRepository;
import com.alzzaipo.hexagonal.portfolio.application.out.FindMemberPortfoliosPort;
import com.alzzaipo.hexagonal.portfolio.application.out.FindPortfolioPort;
import com.alzzaipo.hexagonal.portfolio.application.out.RegisterPortfolioPort;
import com.alzzaipo.hexagonal.portfolio.application.out.UpdatePortfolioPort;
import com.alzzaipo.hexagonal.portfolio.domain.Portfolio;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class PortfolioPersistenceAdapter implements
        RegisterPortfolioPort,
        FindMemberPortfoliosPort,
        FindPortfolioPort,
        UpdatePortfolioPort {

    private final NewPortfolioRepository portfolioRepository;
    private final NewIpoRepository ipoRepository;
    private final NewMemberRepository memberRepository;
    private final EntityManager entityManager;

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

    @Override
    @Transactional(readOnly = true)
    public List<Portfolio> findMemberPortfolios(Uid memberUID) {
        return portfolioRepository.findByMemberUID(memberUID.get())
                .stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Portfolio> findPortfolio(Uid portfolioUID) {
        return portfolioRepository.findByPortfolioUID(portfolioUID.get())
                .map(this::toDomainEntity);
    }

    @Override
    public void updatePortfolio(Portfolio portfolio) {
        PortfolioJpaEntity oldEntity = portfolioRepository.findByPortfolioUID(portfolio.getPortfolioUID().get())
                .orElseThrow(() -> new RuntimeException("포트폴리오 조회 실패"));

        IpoJpaEntity newIpoJpaEntity = ipoRepository.findByStockCode(portfolio.getStockCode())
                .orElseThrow(() -> new RuntimeException("공모주 조회 실패"));

        PortfolioJpaEntity newEntity = toJpaEntity(
                oldEntity.getMemberJpaEntity(),
                newIpoJpaEntity,
                portfolio);

        newEntity.setId(oldEntity.getId());

        entityManager.merge(newEntity);
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

    private Portfolio toDomainEntity(PortfolioJpaEntity portfolioJpaEntity) {
        return new Portfolio(
                new Uid(portfolioJpaEntity.getPortfolioUID()),
                new Uid(portfolioJpaEntity.getMemberJpaEntity().getUid()),
                portfolioJpaEntity.getIpoJpaEntity().getStockName(),
                portfolioJpaEntity.getIpoJpaEntity().getStockCode(),
                portfolioJpaEntity.getSharesCnt(),
                portfolioJpaEntity.getProfit(),
                portfolioJpaEntity.getProfitRate(),
                portfolioJpaEntity.getAgents(),
                portfolioJpaEntity.getMemo());
    }
}
