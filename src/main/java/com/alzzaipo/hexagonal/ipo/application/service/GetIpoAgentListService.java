package com.alzzaipo.hexagonal.ipo.application.service;

import com.alzzaipo.hexagonal.ipo.application.port.in.GetIpoAgentListQuery;
import com.alzzaipo.hexagonal.ipo.application.port.out.FindIpoByStockCodePort;
import com.alzzaipo.hexagonal.ipo.domain.Ipo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetIpoAgentListService implements GetIpoAgentListQuery {

    private final FindIpoByStockCodePort findIpoByStockCodePort;

    @Override
    public List<String> getIpoAgentList(int stockCode) {
        String separator = ",";

        Ipo ipo = findIpoByStockCodePort.findIpoByStockCodePort(stockCode)
                .orElseThrow(() -> new IllegalArgumentException("종목코드 조회 실패"));

        if (ipo.getAgents() == null || ipo.getAgents().isBlank()) {
            throw new IllegalArgumentException("주관사 조회 실패");
        }

        return Arrays.asList(ipo.getAgents().split(separator));
    }
}
