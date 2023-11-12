package com.alzzaipo.portfolio.adapter.out.persistence;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.ipo.adapter.out.persistence.IpoJpaEntity;
import com.alzzaipo.ipo.adapter.out.persistence.NewIpoRepository;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.member.adapter.out.persistence.member.NewMemberRepository;
import com.alzzaipo.portfolio.application.out.*;
import com.alzzaipo.portfolio.domain.Portfolio;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        UpdatePortfolioPort,
        DeletePortfolioPort {

    private final NewPortfolioRepository portfolioRepository;
    private final NewIpoRepository ipoRepository;
    private final NewMemberRepository memberRepository;
    private final EntityManager entityManager;

    @Override
    public void registerPortfolio(Portfolio portfolio) {
        MemberJpaEntity memberJpaEntity = memberRepository.findByUid(portfolio.getMemberUID().get())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "회원 조회 실패"));

        IpoJpaEntity ipoJpaEntity = ipoRepository.findByStockCode(portfolio.getStockCode())
                .orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "공모주 조회 실패"));

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
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "포트폴리오 조회 실패"));

        IpoJpaEntity newIpoJpaEntity = ipoRepository.findByStockCode(portfolio.getStockCode())
                .orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "공모주 조회 실패"));

        PortfolioJpaEntity newEntity = toJpaEntity(
                oldEntity.getMemberJpaEntity(),
                newIpoJpaEntity,
                portfolio);

        newEntity.setPortfolioUID(oldEntity.getPortfolioUID());

        entityManager.merge(newEntity);
    }

    @Override
    public void deletePortfolio(Uid portfolioUID) {
        PortfolioJpaEntity entity = portfolioRepository.findByPortfolioUID(portfolioUID.get())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "포트폴리오 조회 실패"));

        portfolioRepository.delete(entity);
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
