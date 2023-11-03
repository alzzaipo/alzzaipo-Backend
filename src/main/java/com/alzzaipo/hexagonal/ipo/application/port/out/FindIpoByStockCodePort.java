package com.alzzaipo.hexagonal.ipo.application.port.out;

import com.alzzaipo.hexagonal.ipo.domain.Ipo;

import java.util.Optional;

public interface FindIpoByStockCodePort {

    Optional<Ipo> findIpoByStockCodePort(int stockCode);
}
