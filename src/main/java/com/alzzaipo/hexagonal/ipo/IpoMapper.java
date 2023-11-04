package com.alzzaipo.hexagonal.ipo;

import com.alzzaipo.hexagonal.ipo.adapter.out.persistence.IpoJpaEntity;
import com.alzzaipo.hexagonal.ipo.application.port.out.dto.ScrapeIpoResult;
import com.alzzaipo.hexagonal.ipo.domain.Ipo;
import org.springframework.stereotype.Component;

@Component
public class IpoMapper {

    public Ipo toDomainEntity(ScrapeIpoResult scrapeIpoResult) {
        return new Ipo(
                scrapeIpoResult.getStockName(),
                scrapeIpoResult.getStockCode(),
                scrapeIpoResult.getExpectedOfferingPriceMin(),
                scrapeIpoResult.getExpectedOfferingPriceMax(),
                scrapeIpoResult.getFixedOfferingPrice(),
                scrapeIpoResult.getTotalAmount(),
                scrapeIpoResult.getCompetitionRate(),
                scrapeIpoResult.getLockupRate(),
                scrapeIpoResult.getAgents(),
                scrapeIpoResult.getSubscribeStartDate(),
                scrapeIpoResult.getSubscribeEndDate(),
                scrapeIpoResult.getListedDate(),
                0, 0);
    }

    public Ipo toDomainEntity(IpoJpaEntity ipoJpaEntity) {
        return new Ipo(
                ipoJpaEntity.getStockName(),
                ipoJpaEntity.getStockCode(),
                ipoJpaEntity.getExpectedOfferingPriceMin(),
                ipoJpaEntity.getExpectedOfferingPriceMax(),
                ipoJpaEntity.getFixedOfferingPrice(),
                ipoJpaEntity.getTotalAmount(),
                ipoJpaEntity.getCompetitionRate(),
                ipoJpaEntity.getLockupRate(),
                ipoJpaEntity.getAgents(),
                ipoJpaEntity.getSubscribeStartDate(),
                ipoJpaEntity.getSubscribeEndDate(),
                ipoJpaEntity.getListedDate(),
                ipoJpaEntity.getInitialMarketPrice(),
                ipoJpaEntity.getProfitRate());
    }
}
