package com.alzzaipo.ipo.adapter.out.persistence;

import com.alzzaipo.ipo.IpoMapper;
import com.alzzaipo.ipo.application.port.in.dto.AnalyzeIpoProfitRateCommand;
import com.alzzaipo.ipo.application.port.out.*;
import com.alzzaipo.ipo.application.port.out.dto.UpdateListedIpoCommand;
import com.alzzaipo.ipo.domain.Ipo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IpoPersistenceAdapter implements
        RegisterIpoPort,
        FindIpoByStockCodePort,
        FindIpoListPort,
        FindAnalyzeIpoProfitRateTargetPort,
        FindNotListedIposPort,
        UpdateListedIpoPort {

    private final NewIpoRepository ipoRepository;
    private final IpoMapper ipoMapper;

    @Override
    public void registerIpo(Ipo ipo) {
        IpoJpaEntity ipoJpaEntity = new IpoJpaEntity(
                ipo.getStockName(),
                ipo.getStockCode(),
                ipo.getExpectedOfferingPriceMin(),
                ipo.getExpectedOfferingPriceMax(),
                ipo.getFixedOfferingPrice(),
                ipo.getTotalAmount(),
                ipo.getCompetitionRate(),
                ipo.getLockupRate(),
                ipo.getAgents(),
                ipo.getSubscribeStartDate(),
                ipo.getSubscribeEndDate(),
                ipo.getListedDate(),
                ipo.getInitialMarketPrice(),
                ipo.getProfitRate());

        ipoRepository.save(ipoJpaEntity);
    }

    @Override
    public Optional<Ipo> findIpoByStockCodePort(int stockCode) {
        return ipoRepository.findByStockCode(stockCode)
                .map(this.ipoMapper::toDomainEntity);
    }

    @Override
    public List<Ipo> findIpoList() {
        return ipoRepository.findAll()
                .stream()
                .map(this.ipoMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ipo> findAnalyzeIpoProfitRateTarget(AnalyzeIpoProfitRateCommand command) {
        return ipoRepository.findAnalyzeIpoProfitRateTarget(
                        command.getYearFrom(),
                        command.getYearTo(),
                        command.getMinCompetitionRate(),
                        command.getMinLockupRate()
                )
                .stream()
                .map(this.ipoMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ipo> findNotListedIpos() {
        return ipoRepository.findNotListedIpos()
                .stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateListedIpo(UpdateListedIpoCommand command) {
        ipoRepository.findByStockCode(command.getStockCode())
                .ifPresent(entity -> {
                    entity.changeProfitRate(command.getProfitRate());
                    entity.changeInitialMarketPrice(command.getInitialMarketPrice());
                    entity.setListed();
                });
    }

    private Ipo toDomainEntity(IpoJpaEntity jpaEntity) {
        return new Ipo(
                jpaEntity.getStockName(),
                jpaEntity.getStockCode(),
                jpaEntity.getExpectedOfferingPriceMin(),
                jpaEntity.getExpectedOfferingPriceMax(),
                jpaEntity.getFixedOfferingPrice(),
                jpaEntity.getTotalAmount(),
                jpaEntity.getCompetitionRate(),
                jpaEntity.getLockupRate(),
                jpaEntity.getAgents(),
                jpaEntity.getSubscribeStartDate(),
                jpaEntity.getSubscribeEndDate(),
                jpaEntity.getListedDate(),
                jpaEntity.getInitialMarketPrice(),
                jpaEntity.getProfitRate());
    }
}
