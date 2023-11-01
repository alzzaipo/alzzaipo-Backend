package com.alzzaipo.hexagonal.ipo.adapter.out.persistence;

import com.alzzaipo.hexagonal.ipo.application.port.out.RegisterIpoPort;
import com.alzzaipo.hexagonal.ipo.domain.Ipo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IpoPersistenceAdapter implements RegisterIpoPort {

    private final IpoRepository ipoRepository;

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
}
