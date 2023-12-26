package com.alzzaipo.ipo.application.port.out.dto;

public interface CheckIpoRegisteredPort {

    boolean existsByStockCode(int stockCode);
}
