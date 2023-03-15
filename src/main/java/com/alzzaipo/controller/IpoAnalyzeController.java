package com.alzzaipo.controller;

import com.alzzaipo.service.IpoService;
import com.alzzaipo.domain.dto.IpoAnalyzeRequestDto;
import com.alzzaipo.domain.dto.IpoAnalyzeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class IpoAnalyzeController {

    private final IpoService ipoService;

    @GetMapping("/analyze")
    public String createIpoAnalyzeForm(Model model) {
        IpoAnalyzeRequestDto requestDto = new IpoAnalyzeRequestDto();
        model.addAttribute("requestDto", requestDto);
        return "analyze/createAnalyzeForm";
    }

    @PostMapping("/analyze")
    public String analyzedIPOList(IpoAnalyzeRequestDto requestDto, Model model) {
        List<IpoAnalyzeResponseDto> responseDtoList = ipoService.analyze(requestDto);
        int averageProfitRate = ipoService.getAverageProfitRateFromIPOAnalyzeResponseDto(responseDtoList);
        model.addAttribute("responseDtoList", responseDtoList);
        model.addAttribute("averageProfitRate", averageProfitRate);
        return "analyze/analyzedIPOList";
    }
}
