package com.alzzaipo.portfolio.adapter.out.persistence;

import com.alzzaipo.common.Id;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.ipo.adapter.out.persistence.IpoJpaEntity;
import com.alzzaipo.ipo.adapter.out.persistence.IpoRepository;
import com.alzzaipo.member.adapter.out.persistence.member.MemberJpaEntity;
import com.alzzaipo.member.adapter.out.persistence.member.MemberRepository;
import com.alzzaipo.portfolio.application.port.out.DeletePortfolioPort;
import com.alzzaipo.portfolio.application.port.out.FindMemberPortfoliosPort;
import com.alzzaipo.portfolio.application.port.out.FindPortfolioPort;
import com.alzzaipo.portfolio.application.port.out.RegisterPortfolioPort;
import com.alzzaipo.portfolio.application.port.out.UpdatePortfolioPort;
import com.alzzaipo.portfolio.domain.Portfolio;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
@RequiredArgsConstructor
public class PortfolioPersistenceAdapter implements
    RegisterPortfolioPort,
    FindMemberPortfoliosPort,
    FindPortfolioPort,
    UpdatePortfolioPort,
    DeletePortfolioPort {

    private final PortfolioRepository portfolioRepository;
    private final IpoRepository ipoRepository;
    private final MemberRepository memberRepository;
    private final EntityManager entityManager;

    @Override
    public void registerPortfolio(Portfolio portfolio) {
        MemberJpaEntity memberJpaEntity =
            memberRepository.findEntityById(portfolio.getMemberId().get());

        IpoJpaEntity ipoJpaEntity = ipoRepository.findByStockCode(portfolio.getStockCode())
            .orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "공모주 조회 실패"));

        PortfolioJpaEntity portfolioJpaEntity = toJpaEntity(
            memberJpaEntity,
            ipoJpaEntity,
            portfolio);

        portfolioRepository.save(portfolioJpaEntity);
    }

    @Override
    public List<Portfolio> findMemberPortfolios(Id memberId) {
        return portfolioRepository.findByMemberJpaEntityId(memberId.get())
            .stream()
            .map(this::toDomainEntity)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Portfolio> findPortfolio(Id portfolioId) {
        return portfolioRepository.findById(portfolioId.get())
            .map(this::toDomainEntity);
    }

    @Override
    public void updatePortfolio(Portfolio portfolio) {
        PortfolioJpaEntity oldEntity = portfolioRepository.findById(portfolio.getPortfolioId().get())
            .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "포트폴리오 조회 실패"));

        IpoJpaEntity newIpoJpaEntity = ipoRepository.findByStockCode(portfolio.getStockCode())
            .orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "공모주 조회 실패"));

        PortfolioJpaEntity newEntity = toJpaEntity(
            oldEntity.getMemberJpaEntity(),
            newIpoJpaEntity,
            portfolio);

        newEntity.setId(oldEntity.getId());

        entityManager.merge(newEntity);
    }

    @Override
    public void deletePortfolio(Id portfolioId) {
        PortfolioJpaEntity entity = portfolioRepository.findById(portfolioId.get())
            .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "포트폴리오 조회 실패"));

        portfolioRepository.delete(entity);
    }

    private PortfolioJpaEntity toJpaEntity(MemberJpaEntity memberJpaEntity, IpoJpaEntity ipoJpaEntity, Portfolio portfolio) {
        return new PortfolioJpaEntity(
            portfolio.getPortfolioId().get(),
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
            new Id(portfolioJpaEntity.getId()),
            new Id(portfolioJpaEntity.getMemberJpaEntity().getId()),
            portfolioJpaEntity.getIpoJpaEntity().getStockName(),
            portfolioJpaEntity.getIpoJpaEntity().getStockCode(),
            portfolioJpaEntity.getSharesCnt(),
            portfolioJpaEntity.getProfit(),
            portfolioJpaEntity.getProfitRate(),
            portfolioJpaEntity.getAgents(),
            portfolioJpaEntity.getMemo());
    }
}
