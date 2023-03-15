package com.alzzaipo.controller;

import com.alzzaipo.config.SessionConfig;
import com.alzzaipo.config.SessionManager;
import com.alzzaipo.service.IpoService;
import com.alzzaipo.service.PortfolioService;
import com.alzzaipo.domain.dto.PortfolioCreateRequestDto;
import com.alzzaipo.domain.dto.PortfolioListDto;
import com.alzzaipo.domain.dto.PortfolioUpdateDto;
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

        if(!sessionManager.verifySession(session)) {
            return "login";
        }

        PortfolioCreateRequestDto portfolioCreateRequestDto = new PortfolioCreateRequestDto();

        model.addAttribute("portfolioCreateRequestDto", portfolioCreateRequestDto);
        model.addAttribute("ipoList", ipoService.getAllDtoList());
        return "portfolio/createPortfolioForm";
    }

    @PostMapping("/portfolio/new")
    public String create(HttpSession session, PortfolioCreateRequestDto portfolioCreateRequestDto) {

        if(!sessionManager.verifySession(session)) {
            return "login";
        }

        Long memberId = getMemberId(session);
        portfolioService.createMemberPortfolio(memberId, portfolioCreateRequestDto);
        return "redirect:/portfolio";
    }

    @GetMapping("portfolio/{portfolioId}/edit")
    public String updateForm(@PathVariable("portfolioId") Long portfolioId, Model model) {
        Optional<PortfolioUpdateDto> portfolioUpdateDto = portfolioService.getPortfolioUpdateDto(portfolioId);

        if(portfolioUpdateDto.isEmpty()) {
            log.error("해당하는 포트폴리오를 찾지 못했습니다. portfolioId=" + portfolioId);
            return "/portfolio/showPortfolioList";
        }

        model.addAttribute("portfolioUpdateDto", portfolioUpdateDto.get());
        return "portfolio/updatePortfolioForm";
    }

    @PutMapping("portfolio/{portfolioId}/edit")
    public String update(HttpSession session, PortfolioUpdateDto portfolioUpdateDto) {
        Long memberId = getMemberId(session);
        portfolioService.updateMemberPortfolio(memberId, portfolioUpdateDto);
        return "redirect:/portfolio";
    }

    @DeleteMapping("portfolio/{portfolioId}/remove")
    public String delete(HttpSession session, @PathVariable("portfolioId") Long portfolioId) {
        Long memberId = getMemberId(session);
        portfolioService.deleteMemberPortfolio(memberId, portfolioId);
        return "redirect:/portfolio";
    }

    private Long getMemberId(HttpSession session) {
        Long memberId = (Long) session.getAttribute(SessionConfig.memberId);
        return memberId;
    }

}
