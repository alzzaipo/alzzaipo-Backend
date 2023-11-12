package com.alzzaipo.ipo.application.service;

import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.ipo.application.port.in.GetIpoAgentListQuery;
import com.alzzaipo.ipo.application.port.out.FindIpoByStockCodePort;
import com.alzzaipo.ipo.domain.Ipo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "종목코드 조회 실패"));

        if (ipo.getAgents() == null || ipo.getAgents().isBlank()) {
            throw new CustomException(HttpStatus.NOT_FOUND, "주관사 조회 실패");
        }

        return Arrays.asList(ipo.getAgents().split(separator));
    }
}
