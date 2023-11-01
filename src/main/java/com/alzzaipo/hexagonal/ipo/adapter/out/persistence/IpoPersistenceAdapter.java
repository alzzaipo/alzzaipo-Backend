package com.alzzaipo.hexagonal.ipo.adapter.out.persistence;

import com.alzzaipo.hexagonal.ipo.IpoMapper;
import com.alzzaipo.hexagonal.ipo.application.port.out.FindIpoByStockCodePort;
import com.alzzaipo.hexagonal.ipo.application.port.out.RegisterIpoPort;
import com.alzzaipo.hexagonal.ipo.domain.Ipo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class IpoPersistenceAdapter implements
        RegisterIpoPort
        , FindIpoByStockCodePort {

    private final IpoRepository ipoRepository;
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
}
