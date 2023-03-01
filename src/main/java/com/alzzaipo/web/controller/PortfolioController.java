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
    public String getMemberPortfolios(HttpSession session, Model model) {

        if(!sessionManager.verifySession(session)) {
            return "login";
        }

        Long memberId = getMemberId(session);

        List<PortfolioListDto> portfolioListDtos = portfolioService.getMemberPortfolioListDtos(memberId);

        model.addAttribute("portfolioListDtos", portfolioListDtos);
        return "portfolio/showPortfolioList";
    }

    @GetMapping("/portfolio/new")
    public String createPortfolioForm(HttpSession session, Model model) {
        System.out.println("called");

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

        Long memberId = getMemberId(session);

        portfolioService.createMemberPortfolio(memberId, portfolioSaveRequestDto);

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
    public String update(HttpSession session ,PortfolioSaveRequestDto portfolioSaveRequestDto) {
        Long memberId = getMemberId(session);

        portfolioService.updateMemberPortfolio(memberId, portfolioSaveRequestDto);

        return "redirect:/portfolio";
    }

    @DeleteMapping("portfolio/{portfolioId}/remove")
    public String delete(HttpSession session, @PathVariable("portfolioId") Long portfolioId) {
        Long memberId = getMemberId(session);
        Portfolio portfolio = portfolioService.findPortfolioById(portfolioId);
        portfolioService.deleteMemberPortfolio(memberId, portfolio);
        return "redirect:/portfolio";
    }

    private Long getMemberId(HttpSession session) {
        Long memberId = (Long) session.getAttribute(SessionConfig.memberId);
        return memberId;
    }

}
