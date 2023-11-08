package com.alzzaipo.ipo.adapter.in.web;

import com.alzzaipo.ipo.application.port.in.AnalyzeIpoProfitRateQuery;
import com.alzzaipo.ipo.application.port.in.GetIpoAgentListQuery;
import com.alzzaipo.ipo.application.port.in.GetIpoListQuery;
import com.alzzaipo.ipo.application.port.in.dto.AnalyzeIpoProfitRateCommand;
import com.alzzaipo.ipo.application.port.in.dto.AnalyzeIpoProfitRateResult;
import com.alzzaipo.ipo.domain.Ipo;
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
