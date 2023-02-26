package com.alzzaipo.web.controller;

import com.alzzaipo.config.SessionConfig;
import com.alzzaipo.config.SessionManager;
import com.alzzaipo.service.IpoService;
import com.alzzaipo.service.PortfolioService;
import com.alzzaipo.web.domain.Portfolio.Portfolio;
import com.alzzaipo.web.dto.PortfolioListDto;
import com.alzzaipo.web.dto.PortfolioSaveRequestDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PortfolioController {

    private final SessionManager sessionManager;
    private final IpoService ipoService;
    private final PortfolioService portfolioService;

    @GetMapping("/portfolio")
    public String showPortfolioList(HttpSession session, Model model) {

        if(!sessionManager.verifySession(session)) {
            return "login";
        }

        Long memberId = (Long) session.getAttribute(SessionConfig.memberId);

        List<PortfolioListDto> portfolioListDtos = portfolioService.getPortfolioListDtosByMemberId(memberId);

        model.addAttribute("portfolioListDtos", portfolioListDtos);
        return "portfolio/showPortfolioList";
    }

    @GetMapping("/portfolio/new")
    public String createForm(HttpSession session, Model model) {

        if(!sessionManager.verifySession(session)) {
            return "login";
        }

        PortfolioSaveRequestDto portfolioSaveRequestDto = new PortfolioSaveRequestDto();

        model.addAttribute("portfolioSaveRequestDto", portfolioSaveRequestDto);
        model.addAttribute("ipoList", ipoService.getAllDtoList());
        return "portfolio/createPortfolioForm";
    }

    @PostMapping("/portfolio/new")
    public String create(HttpSession session, PortfolioSaveRequestDto portfolioSaveRequestDto) {

        if(!sessionManager.verifySession(session)) {
            return "login";
        }

        Long memberId = (Long) session.getAttribute(SessionConfig.memberId);
        if(memberId != portfolioSaveRequestDto.getMemberId()) {
            return "index";
        }

        portfolioService.createPortfolio(portfolioSaveRequestDto);

        return "redirect:/portfolio";
    }

    @GetMapping("portfolio/{portfolioId}/edit")
    public String updateForm(@PathVariable("portfolioId") Long portfolioId, Model model) {
        Optional<PortfolioSaveRequestDto> portfolioSaveRequestDto = portfolioService.getPortfolioSaveRequestDto(portfolioId);

        if(portfolioSaveRequestDto.isEmpty()) {
            log.error("해당하는 포트폴리오를 찾지 못했습니다. portfolioId=" + portfolioId);
            return "/portfolio/showPortfolioList";
        }

        model.addAttribute("portfolioSaveRequestDto", portfolioSaveRequestDto.get());
        return "portfolio/updatePortfolioForm";
    }

    @PutMapping("portfolio/{portfolioId}/edit")
    public String update(PortfolioSaveRequestDto portfolioSaveRequestDto) {
        Portfolio portfolio = portfolioService.fromSaveRequestDtoToEntity(portfolioSaveRequestDto);
        System.out.println("portfolio = " + portfolio.getId());

        if(portfolio != null) {
            portfolioService.save(portfolio);
        } else {
            log.error("Portfolio Update Failed. portfolioId=" + portfolioSaveRequestDto.getPortfolioId());
        }

        return "redirect:/portfolio";
    }

    @DeleteMapping("portfolio/{portfolioId}/remove")
    public String delete(@PathVariable("portfolioId") Long portfolioId) {
        Portfolio portfolio = portfolioService.findPortfolioById(portfolioId);
        portfolioService.delete(portfolio);
        return "redirect:/portfolio";
    }

}
