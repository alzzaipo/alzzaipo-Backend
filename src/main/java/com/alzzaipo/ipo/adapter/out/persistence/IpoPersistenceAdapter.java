package com.alzzaipo.ipo.adapter.out.persistence;

import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.ipo.application.port.in.dto.AnalyzeIpoProfitRateCommand;
import com.alzzaipo.ipo.application.port.out.CheckIpoExistsPort;
import com.alzzaipo.ipo.application.port.out.FindAnalyzeIpoProfitRateTargetsPort;
import com.alzzaipo.ipo.application.port.out.FindIpoByStockCodePort;
import com.alzzaipo.ipo.application.port.out.FindIpoListPort;
import com.alzzaipo.ipo.application.port.out.FindNotListedIposPort;
import com.alzzaipo.ipo.application.port.out.SaveIposPort;
import com.alzzaipo.ipo.application.port.out.UpdateListedIpoPort;
import com.alzzaipo.ipo.domain.Ipo;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IpoPersistenceAdapter implements SaveIposPort,
    FindIpoByStockCodePort,
    FindIpoListPort,
    FindAnalyzeIpoProfitRateTargetsPort,
    FindNotListedIposPort,
    UpdateListedIpoPort,
    CheckIpoExistsPort {

    private final IpoRepository ipoRepository;

    @Override
    public void saveAll(List<Ipo> ipos) {
        List<IpoJpaEntity> ipoJpaEntities = ipos.parallelStream()
            .map(IpoJpaEntity::build)
            .toList();

        ipoRepository.saveAll(ipoJpaEntities);
    }

    @Override
    public Ipo findByStockCode(int stockCode) {
        return ipoRepository.findByStockCode(stockCode)
            .map(IpoJpaEntity::toDomainEntity)
            .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "공모주 조회 실패"));
    }

    @Override
    public List<Ipo> findIpoList() {
        return ipoRepository.findAll()
            .stream()
            .map(IpoJpaEntity::toDomainEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<Ipo> findTargets(AnalyzeIpoProfitRateCommand command) {
        return ipoRepository.findAnalyzeIpoProfitRateTarget(
                command.getYearFrom(),
                command.getYearTo(),
                command.getMinCompetitionRate(),
                command.getMinLockupRate())
            .stream()
            .map(IpoJpaEntity::toDomainEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<Ipo> findNotListedIpos() {
        return ipoRepository.findNotListedIpos()
            .stream()
            .map(IpoJpaEntity::toDomainEntity)
            .collect(Collectors.toList());
    }

    @Override
    public void updateListedIpo(Ipo ipo) {
        ipoRepository.findByStockCode(ipo.getStockCode())
            .ifPresent(entity -> entity.updateListedWithIpo(ipo));
    }

    @Override
    public boolean existsByStockCode(int stockCode) {
        return ipoRepository.existsByStockCode(stockCode);
    }
}
