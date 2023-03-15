package com.alzzaipo.controller;

import com.alzzaipo.domain.dto.PortfolioCreateRequestDto;
import com.alzzaipo.domain.dto.PortfolioListResponseDto;
import com.alzzaipo.domain.dto.PortfolioUpdateResponseDto;
import com.alzzaipo.service.IpoService;
import com.alzzaipo.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PortfolioController {

    private final IpoService ipoService;
    private final PortfolioService portfolioService;

    @GetMapping("/portfolio")
    public String getMemberPortfolios(@AuthenticationPrincipal String accountId, Model model) {

        List<PortfolioListResponseDto> portfolioListResponseDtos
                = portfolioService.getMemberPortfolioListResponseDtos(accountId);

        model.addAttribute("portfolioListResponseDtos", portfolioListResponseDtos);
        return "portfolio/showPortfolioList";
    }

    @GetMapping("/portfolio/new")
    public String createPortfolioForm(Model model) {

        PortfolioCreateRequestDto portfolioCreateRequestDto = new PortfolioCreateRequestDto();

        model.addAttribute("portfolioCreateRequestDto", portfolioCreateRequestDto);
        model.addAttribute("ipoList", ipoService.getAllDtoList());
        return "portfolio/createPortfolioForm";
    }

    @PostMapping("/portfolio/new")
    public String create(@AuthenticationPrincipal String accountId, PortfolioCreateRequestDto portfolioCreateRequestDto) {

        portfolioService.createMemberPortfolio(accountId, portfolioCreateRequestDto);
        return "redirect:/portfolio";
    }

    @GetMapping("portfolio/{portfolioId}/edit")
    public String updateForm(@PathVariable("portfolioId") Long portfolioId, Model model) {
        Optional<PortfolioUpdateResponseDto> portfolioUpdateResponseDto = portfolioService.getPortfolioUpdateResponseDto(portfolioId);

        if(portfolioUpdateResponseDto.isEmpty()) {
            log.error("해당하는 포트폴리오를 찾지 못했습니다. portfolioId=" + portfolioId);
            return "/portfolio/showPortfolioList";
        }

        model.addAttribute("portfolioUpdateResponseDto", portfolioUpdateResponseDto.get());
        return "portfolio/updatePortfolioForm";
    }

    @PutMapping("portfolio/{portfolioId}/edit")
    public String update(@AuthenticationPrincipal String accountId, PortfolioUpdateResponseDto portfolioUpdateResponseDto) {
        portfolioService.updateMemberPortfolio(accountId, portfolioUpdateResponseDto);
        return "redirect:/portfolio";
    }

    @DeleteMapping("portfolio/{portfolioId}/remove")
    public String delete(@AuthenticationPrincipal String accountId, @PathVariable("portfolioId") Long portfolioId) {
        portfolioService.deleteMemberPortfolio(accountId, portfolioId);
        return "redirect:/portfolio";
    }

}
