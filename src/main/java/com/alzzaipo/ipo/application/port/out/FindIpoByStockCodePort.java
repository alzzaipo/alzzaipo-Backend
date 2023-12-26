package com.alzzaipo.ipo.application.port.out;

import com.alzzaipo.ipo.domain.Ipo;

public interface FindIpoByStockCodePort {

    Ipo findByStockCode(int stockCode);
}
