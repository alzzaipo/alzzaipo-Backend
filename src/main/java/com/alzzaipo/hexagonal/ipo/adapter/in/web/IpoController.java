package com.alzzaipo.hexagonal.ipo.adapter.in.web;

import com.alzzaipo.hexagonal.ipo.application.port.in.*;
import com.alzzaipo.hexagonal.ipo.domain.Ipo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ipo")
@RequiredArgsConstructor
public class IpoController {

    private final GetIpoListQuery getIpoListQuery;
    private final AnalyzeIpoProfitRateQuery analyzeIpoProfitRateQuery;
    private final GetIpoAgentListQuery getIpoAgentListQuery;

    @GetMapping("/list")
    public ResponseEntity<List<Ipo>> getIpoList() {
        List<Ipo> ipoList = getIpoListQuery.getIpoList();
        return ResponseEntity.ok(ipoList);
    }

    @GetMapping("/analyze")
    public ResponseEntity<AnalyzeIpoProfitRateResult> getIpoAnalyzeResult(
            @RequestParam int from,
            @RequestParam int to,
            @RequestParam int minCompetitionRate,
            @RequestParam int minLockupRate
    ) {
        AnalyzeIpoProfitRateCommand analyzeIpoProfitRateCommand
                = new AnalyzeIpoProfitRateCommand(from, to, minCompetitionRate, minLockupRate);

        AnalyzeIpoProfitRateResult analyzeIpoProfitRateResult
                = analyzeIpoProfitRateQuery.analyzeIpoProfitRate(analyzeIpoProfitRateCommand);

        return ResponseEntity.ok(analyzeIpoProfitRateResult);
    }

    @GetMapping("/{stockCode}/agents")
    public ResponseEntity<List<String>> getIpoAgentsList(@PathVariable int stockCode) {
        List<String> ipoAgentList = getIpoAgentListQuery.getIpoAgentList(stockCode);
        return ResponseEntity.ok(ipoAgentList);
    }
}
