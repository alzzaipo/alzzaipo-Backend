package com.alzzaipo.api;

import com.alzzaipo.service.IpoService;
import com.alzzaipo.domain.dto.IpoAnalyzeRequestDto;
import com.alzzaipo.domain.dto.IpoAnalyzeResponseDto;
import com.alzzaipo.domain.dto.IpoListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/ipo")
@RestController
public class IpoRestController {

    private final IpoService ipoService;

    @GetMapping("/list")
    public ResponseEntity<List<IpoListResponseDto>> getIpoList() {
        List<IpoListResponseDto> ipoList = ipoService.getAllDtoList();
        return ResponseEntity.ok(ipoList);
    }

    @GetMapping("/analyze")
    public ResponseEntity<List<IpoAnalyzeResponseDto>> getIpoAnalyzeResult(IpoAnalyzeRequestDto ipoAnalyzeRequestDto) {
        List<IpoAnalyzeResponseDto> responseDtoList = ipoService.analyze(ipoAnalyzeRequestDto);
        return ResponseEntity.ok(responseDtoList);
    }

}