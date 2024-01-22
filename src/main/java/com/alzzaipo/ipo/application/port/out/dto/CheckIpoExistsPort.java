package com.alzzaipo.ipo.application.port.out.dto;

public interface CheckIpoExistsPort {

    boolean existsByStockCode(int stockCode);
}
