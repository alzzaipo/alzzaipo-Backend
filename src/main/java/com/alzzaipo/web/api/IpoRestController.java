package com.alzzaipo.web.api;

import com.alzzaipo.service.IpoService;
import com.alzzaipo.web.dto.IpoAnalyzeRequestDto;
import com.alzzaipo.web.dto.IpoAnalyzeResponseDto;
import com.alzzaipo.web.dto.IpoListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class IpoRestController {

    private final IpoService ipoService;

    @GetMapping("/getIpoAnalyzeResult")
    public ResponseEntity<List<IpoAnalyzeResponseDto>> getIpoAnalyzeResult(IpoAnalyzeRequestDto ipoAnalyzeRequestDto) {
        List<IpoAnalyzeResponseDto> responseDtoList = ipoService.analyze(ipoAnalyzeRequestDto);
        return ResponseEntity.ok(responseDtoList);
    }

    @GetMapping("/getIpoList")
    public ResponseEntity<List<IpoListResponseDto>> getIpoList() {
        List<IpoListResponseDto> ipoList = ipoService.getAllDtoList();
        return ResponseEntity.ok(ipoList);
    }

}
