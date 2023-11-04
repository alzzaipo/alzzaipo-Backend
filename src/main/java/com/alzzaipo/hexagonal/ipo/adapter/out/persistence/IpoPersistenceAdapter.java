package com.alzzaipo.hexagonal.ipo.adapter.out.persistence;

import com.alzzaipo.hexagonal.ipo.IpoMapper;
import com.alzzaipo.hexagonal.ipo.application.port.in.dto.AnalyzeIpoProfitRateCommand;
import com.alzzaipo.hexagonal.ipo.application.port.out.FindAnalyzeIpoProfitRateTargetPort;
import com.alzzaipo.hexagonal.ipo.application.port.out.FindIpoByStockCodePort;
import com.alzzaipo.hexagonal.ipo.application.port.out.FindIpoListPort;
import com.alzzaipo.hexagonal.ipo.application.port.out.RegisterIpoPort;
import com.alzzaipo.hexagonal.ipo.domain.Ipo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class IpoPersistenceAdapter implements
        RegisterIpoPort,
        FindIpoByStockCodePort,
        FindIpoListPort,
        FindAnalyzeIpoProfitRateTargetPort {

    private final NewIpoRepository newIpoRepository;
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

        newIpoRepository.save(ipoJpaEntity);
    }

    @Override
    public Optional<Ipo> findIpoByStockCodePort(int stockCode) {
        return newIpoRepository.findByStockCode(stockCode)
                .map(this.ipoMapper::toDomainEntity);
    }

    @Override
    public List<Ipo> findIpoList() {
        return newIpoRepository.findAll()
                .stream()
                .map(this.ipoMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ipo> findAnalyzeIpoProfitRateTarget(AnalyzeIpoProfitRateCommand command) {
        return newIpoRepository.findAnalyzeIpoProfitRateTarget(
                        command.getYearFrom(),
                        command.getYearTo(),
                        command.getMinCompetitionRate(),
                        command.getMinLockupRate()
                )
                .stream()
                .map(this.ipoMapper::toDomainEntity)
                .collect(Collectors.toList());
    }
}
