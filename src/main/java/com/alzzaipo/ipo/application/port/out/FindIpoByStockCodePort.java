package com.alzzaipo.ipo.application.port.out;

import com.alzzaipo.ipo.domain.Ipo;

import java.util.Optional;

public interface FindIpoByStockCodePort {

    Optional<Ipo> findIpoByStockCodePort(int stockCode);
}
