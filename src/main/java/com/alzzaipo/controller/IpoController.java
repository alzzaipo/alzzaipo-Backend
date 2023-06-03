package com.alzzaipo.controller;

import com.alzzaipo.dto.ipo.IpoAgentsListDto;
import com.alzzaipo.service.IpoService;
import com.alzzaipo.dto.ipo.IpoAnalyzeRequestDto;
import com.alzzaipo.dto.ipo.IpoAnalyzeResponseDto;
import com.alzzaipo.dto.ipo.IpoListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/ipo")
@RestController
public class IpoController {

    private final IpoService ipoService;

    // 데이터베이스에 등록된 공모주 목록을 반환합니다
    @GetMapping("/list")
    public ResponseEntity<List<IpoListDto>> getIpoList() {
        List<IpoListDto> ipoList = ipoService.getIpoList();
        return ResponseEntity.ok(ipoList);
    }

    // 공모주 분석 결과를 반환합니다
    @PostMapping("/analyze")
    public ResponseEntity<IpoAnalyzeResponseDto> getIpoAnalyzeResult(@RequestBody IpoAnalyzeRequestDto ipoAnalyzeRequestDto) {
        // 분석 조건에 부합하는 공모주들의 (평균 수익률, 공모주 목록)을 담은 분석결과 DTO를 가져옵니다
        IpoAnalyzeResponseDto analyzeResponseDto = ipoService.analyze(ipoAnalyzeRequestDto);
        return ResponseEntity.ok(analyzeResponseDto);
    }

    @GetMapping("/{stockCode}/agents")
    public ResponseEntity<IpoAgentsListDto> getIpoAgentsList(@PathVariable int stockCode) {
        IpoAgentsListDto ipoAgentsListDto = ipoService.getIpoAgentsListDto(stockCode);
        return ResponseEntity.ok(ipoAgentsListDto);
    }
}