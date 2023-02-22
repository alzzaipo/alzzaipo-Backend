package com.alzzaipo.web.controller;

import com.alzzaipo.web.dto.IPOAnalyzeRequestDto;
import com.alzzaipo.service.IPOService;
import com.alzzaipo.web.dto.IPOAnalyzeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AnalyzeController {

    private final IPOService ipoService;

    @GetMapping("/analyze")
    public String createAnalyzeForm(Model model) {
        IPOAnalyzeRequestDto requestDto = new IPOAnalyzeRequestDto();
        model.addAttribute("requestDto", requestDto);
        return "analyze/createAnalyzeForm";
    }

    @PostMapping("/analyze")
    public String analyzedIPOList(IPOAnalyzeRequestDto requestDto, Model model) {
        List<IPOAnalyzeResponseDto> responseDtoList = ipoService.analyze(requestDto);
        int averageProfitRate = ipoService.getAverageProfitRateFromIPOAnalyzeResponseDto(responseDtoList);
        model.addAttribute("responseDtoList", responseDtoList);
        model.addAttribute("averageProfitRate", averageProfitRate);
        return "analyze/analyzedIPOList";
    }
}
