package com.alzzaipo.ipo.adapter.in.web;

import com.alzzaipo.ipo.application.port.in.AnalyzeIpoProfitRateQuery;
import com.alzzaipo.ipo.application.port.in.GetIpoAgentListQuery;
import com.alzzaipo.ipo.application.port.in.GetIpoListQuery;
import com.alzzaipo.ipo.application.port.in.dto.AnalyzeIpoProfitRateCommand;
import com.alzzaipo.ipo.application.port.in.dto.AnalyzeIpoProfitRateResult;
import com.alzzaipo.ipo.domain.Ipo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ipo")
@RequiredArgsConstructor
public class IpoController {

    private final GetIpoListQuery getIpoListQuery;
    private final GetIpoAgentListQuery getIpoAgentListQuery;
    private final AnalyzeIpoProfitRateQuery analyzeIpoProfitRateQuery;

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
        AnalyzeIpoProfitRateCommand command = new AnalyzeIpoProfitRateCommand(
            from, to, minCompetitionRate, minLockupRate);
        AnalyzeIpoProfitRateResult analyzeResult = analyzeIpoProfitRateQuery.analyze(command);
        return ResponseEntity.ok(analyzeResult);
    }

    @GetMapping("/{stockCode}/agents")
    public ResponseEntity<List<String>> getIpoAgentsList(@PathVariable int stockCode) {
        List<String> ipoAgentList = getIpoAgentListQuery.getIpoAgentList(stockCode);
        return ResponseEntity.ok(ipoAgentList);
    }
}
